import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import com.google.common.graph.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class to display a graph on screen
 *
 * @author N. Howe
 * @version November 2023
 */
public class GraphDisplay extends JComponent implements ActionListener {
  // only one of the following three should be active
  /** The Graph to display */
  private Graph graph;
  /** The ValueGraph to display */
  private ValueGraph vgraph;
  /** The Network to display */
  private Network net;

  /** Is the graph directed? */
  private boolean isDirected;

  /** Map graph objects to locations */
  private HashMap<Object, Point> locMap;

  /** Map graph objects to colors */
  private HashMap<Object, Color> colorMap;

  /** Map graph objects to labels */
  private HashMap<Object, String> labelMap;

  /** Map graph objects to notes */
  private HashMap<Object, String> noteMap;

  /** Map graph objects to notes */
  private HashMap<Object, ActionListener> callMap;

  /** Window the graph will appear in */
  private JFrame frame;

  /** TImer for callbacks */
  private Timer timer;

  /** Location of current drag */
  private Point dragPoint = null;

  /** Remembers node where last mousedown event occurred */
  private Object activeNode;

  /** Size of canvas */
  public static final Dimension CANVAS_SIZE = new Dimension(500, 300);

  /** Radius of nodes */
  public static final int NODE_RADIUS = 16;

  /** Radius to draw arrows */
  public static final int ARROW_RADIUS = NODE_RADIUS + 3;

  /** default color of nodes */
  public static final Color DEFAULT_NODE_COLOR = new Color(192, 192, 255);

  /** default color of edges */
  public static final Color DEFAULT_EDGE_COLOR = Color.BLACK;

  /** default color of edges */
  public Point labelOffset = new Point(0, 24);

  /** default color of edges */
  public Point noteOffset = new Point(0, -30 - NODE_RADIUS);

  /** used to draw arrows */
  private static final AffineTransform tx = new AffineTransform();

  /** used to draw arrows */
  private static final Line2D.Double line = new Line2D.Double(0, 0, 100, 100);

  /** used to draw arrows */
  private static final Polygon arrowHead = new Polygon();

  static {
    arrowHead.addPoint(0, 4);
    arrowHead.addPoint(-4, -4);
    arrowHead.addPoint(4, -4);
  }

  // Note: because we want this file to work with any sort of graph,
  // we are using the raw Graph type instead of a generic version.
  // This means that Java's type system cannot check the type safety of the code.
  // To prevent a proliferation of warnings, we have liberally sprinkled
  // suppression
  // directives throughout the code. Normally this is not a good idea, unless
  // you have a very good reason and know exactly why you are doing it. ;)
  /** Constructor starts with empty graph */
  @SuppressWarnings("unchecked")
  public GraphDisplay(Graph<?> g, String graphName) {
    super();
    this.graph = g;
    isDirected = g.isDirected();
    commonSetup(graphName);
  }

  /** Constructor starts with empty graph */
  @SuppressWarnings("unchecked")
  public GraphDisplay(ValueGraph<?, ?> g, String graphName) {
    super();
    this.vgraph = g;
    isDirected = g.isDirected();
    commonSetup(graphName);
  }

  /** Constructor starts with empty graph */
  @SuppressWarnings("unchecked")
  public GraphDisplay(Network<?, ?> g, String graphName) {
    super();
    this.net = g;
    isDirected = g.isDirected();
    commonSetup(graphName);
  }

  /** Method to finish the common setup for all three graph types */
  private void commonSetup(String graphName) {
    locMap = new HashMap<Object, Point>();
    assignLocations();
    colorMap = new HashMap<Object, Color>();
    labelMap = new HashMap<Object, String>();
    noteMap = new HashMap<Object, String>();
    callMap = new HashMap<Object, ActionListener>();

    setMinimumSize(CANVAS_SIZE);
    setPreferredSize(CANVAS_SIZE);
    openWindow(graphName);
  }

  /** Assigns nodes to points around an oval */
  public void assignLocations() {
    Set<Object> nodes = getNodeSet();
    int i = 0;
    int num = nodes.size();
    int w = CANVAS_SIZE.width;
    int h = CANVAS_SIZE.height;
    for (Object n : nodes) {
      double angle = Math.PI / 2 + ((2 * i + 0.5) * Math.PI) / num;
      setLoc(n, new Point((int) (w / 2.0 + w * Math.cos(angle) / 2.5), (int) (h / 2.0 + h * Math.sin(angle) / 2.5)));
      i++;
    }
  }

  /** Sets up the GUI window */
  private void openWindow(String graphName) {
    // Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);

    // Create and set up the window.
    frame = new JFrame(graphName);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Add components
    createComponents(frame);

    // Display the window.
    frame.pack();
    frame.setVisible(true);

    // Add listener for drag events
    DragListener dl = new DragListener();
    this.addMouseListener(dl);
    this.addMouseMotionListener(dl);

    // Begin animation callback events
    timer = new Timer(25, this);
    timer.setInitialDelay(500);
    timer.start();
  }

  private void createComponents(JFrame frame) {
    Container pane = frame.getContentPane();
    pane.add(this);
  }

  /** Returns the node under the given location, or null if none */
  public Object getNode(int x, int y) {
    Object result = null;
    for (Object n : getNodeSet()) {
      // System.out.println("Node "+n+": "+getLoc(n).distance(x, y));
      if (getLoc(n).distance(x, y) <= NODE_RADIUS) {
        result = n;
      }
    }
    return result;
  }

  /** Returns the location of a given graph element */
  public Point getLoc(Object obj) {
    Point loc = locMap.get(obj);
    if (loc == null) {
      loc = new Point(ThreadLocalRandom.current().nextInt(0, CANVAS_SIZE.width),
          ThreadLocalRandom.current().nextInt(0, CANVAS_SIZE.height));
      locMap.put(obj, loc);
    }
    return loc;
  }

  /** Sets the location of a given graph element */
  public void setLoc(Object obj, Point loc) {
    locMap.put(obj, loc);
  }

  /** Sets the location of an edge between nodes */
  public void setLoc(Object t, Object h, Point loc) {
    locMap.put(getEdgeBetween(t, h), loc);
  }

  /** Sets multiple node locations at once */
  public void setLocs(HashMap<?, ? extends Point> locs) {
    locMap.putAll(locs);
  }

  /** Returns the color of a given graph element */
  public Color getColor(Object obj) {
    Color c = colorMap.get(obj);
    if (c == null) {
      if (getNodeSet().contains(obj)) {
        c = DEFAULT_NODE_COLOR;
      } else {
        c = DEFAULT_EDGE_COLOR;
      }
    }
    return c;
  }

  /** Sets the color of an edge between nodes */
  public void setColor(Object t, Object h, Color c) {
    colorMap.put(getEdgeBetween(t, h), c);
  }

  /** Sets the color of a given graph element */
  public void setColor(Object obj, Color c) {
    colorMap.put(obj, c);
  }

  /** Sets multiple colors at once */
  public void setColors(HashMap<?, ? extends Color> colors) {
    colorMap.putAll(colors);
  }

  /** Returns the label of a given graph element */
  public String getLabel(Object obj) {
    if (obj == null) {
      return null;
    }
    String lbl = labelMap.get(obj);
    if (lbl == null) {
      lbl = obj.toString();
    }
    return lbl;
  }

  /** Sets the label of a given graph element */
  public void setLabel(Object obj, String lbl) {
    labelMap.put(obj, lbl);
  }

  /** Sets the label of an edge between nodes */
  public void setLabel(Object t, Object h, String lbl) {
    labelMap.put(getEdgeBetween(t, h), lbl);
  }

  /** Sets multiple labels at once */
  public void setLabels(HashMap<?, ? extends String> labels) {
    labelMap.putAll(labels);
  }

  /** Returns the note on a given graph element */
  public String getNote(Object obj) {
    String note = noteMap.get(obj);
    if (note == null) {
      note = "";
    }
    return note;
  }

  /** Sets the note on a given graph element */
  public void setNote(Object obj, String note) {
    noteMap.put(obj, note);
  }

  /** Sets the note on an edge between nodes */
  public void setNote(Object t, Object h, String note) {
    noteMap.put(getEdgeBetween(t, h), note);
  }

  /** Sets multiple labels at once */
  public void setNotes(HashMap<?, ? extends String> notes) {
    noteMap.putAll(notes);
  }

  /** Returns the callback object on a given graph element */
  public ActionListener getCallback(Object obj) {
    ActionListener call = callMap.get(obj);
    return call;
  }

  /** Sets the callback on a given graph element */
  public void setCallback(Object obj, ActionListener call) {
    callMap.put(obj, call);
  }

  /** Sets multiple labels at once */
  public void setCallbacks(HashMap<?, ? extends ActionListener> calls) {
    callMap.putAll(calls);
  }

  /** Reset colors to default */
  public void setNodeColors(Color c) {
    for (Object n : getNodeSet()) {
      colorMap.put(n, c);
    }
  }

  /** Reset colors to default */
  public void setEdgeColors(Color c) {
    for (Object e : getEdgeSet()) {
      colorMap.put(e, c);
    }
  }

  /** returns the node set */
  @SuppressWarnings("unchecked")
  public Set<Object> getNodeSet() {
    Set<Object> nodes;
    if (graph != null) {
      nodes = graph.nodes();
    } else if (vgraph != null) {
      nodes = vgraph.nodes();
    } else if (net != null) {
      nodes = net.nodes();
    } else {
      nodes = new HashSet<Object>();
    }
    return nodes;
  }

  /** returns a representation of the edge between two nodes */
  @SuppressWarnings("unchecked")
  public Object getEdgeBetween(Object n1, Object n2) {
    Object e = null;
    if (((graph != null) && graph.hasEdgeConnecting(n1, n2))
        || ((vgraph != null) && vgraph.hasEdgeConnecting(n1, n2))) {
      if (((graph != null) && graph.isDirected())
          || ((vgraph != null) && vgraph.isDirected())) {
        e = new Pair<Object, Object>(n1, n2);
      } else {
        e = new Diset<Object>(n1, n2);
      }
    } else if (net != null) {
      if (net.hasEdgeConnecting(n1, n2)) {
        e = net.edgeConnecting(n1, n2).get();
      }
    }
    return e;
  }

  /** returns the edge set */
  @SuppressWarnings("unchecked")
  public Set<Object> getEdgeSet() {
    Set<Object> edges = new HashSet<Object>();
    Set<Object> nodes = getNodeSet();
    for (Object n : nodes) {
      if (net != null) {
        edges = net.edges();
      } else {
        Set<Object> succ;
        if (graph != null) {
          succ = graph.successors(n);
        } else if (vgraph != null) {
          succ = graph.successors(n);
        } else {
          return edges;
        }
        for (Object s : succ) {
          edges.add(getEdgeBetween(n, s));
        }
      }
    }
    return edges;
  }

  /** returns the node set */
  @SuppressWarnings("unchecked")
  public Set getAdjacentNodes(Object n) {
    Set edges;
    if (graph != null) {
      edges = graph.successors(n);
    } else if (vgraph != null) {
      edges = vgraph.successors(n);
    } else if (net != null) {
      edges = net.successors(n);
    } else {
      edges = new HashSet<Object>();
    }
    return edges;
  }

  /** simple three-line arrow */
  private void drawArrow(Point p1, Point p2, Graphics2D g2) {
    double d = p1.distance(p2);
    double dx = (p2.x - p1.x) / d;
    double dy = (p2.y - p1.y) / d;
    double dxrot = -dy;
    double dyrot = dx;
    int a0x = (int) (p2.x - ARROW_RADIUS * dx);
    int a0y = (int) (p2.y - ARROW_RADIUS * dy);
    int a1x = (int) (a0x - 5 * dx + 2.5 * dxrot);
    int a1y = (int) (a0y - 5 * dy + 2.5 * dyrot);
    int a2x = (int) (a0x - 5 * dx - 2.5 * dxrot);
    int a2y = (int) (a0y - 5 * dy - 2.5 * dyrot);
    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
    g2.drawLine(a0x, a0y, a1x, a1y);
    g2.drawLine(a0x, a0y, a2x, a2y);
    g2.drawLine(a1x, a1y, a2x, a2y);
  }

  /**
   * for drawing arrows
   * see
   * https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java
   */
  private void oldDrawArrow(Point p1, Point p2, Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setStroke(new BasicStroke(2));
    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
    tx.setToIdentity();
    double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);
    tx.translate(p2.x - ARROW_RADIUS * Math.cos(angle), p2.y - ARROW_RADIUS * Math.sin(angle));
    tx.rotate((angle - Math.PI / 2d));

    g2.setTransform(tx);
    g2.fill(arrowHead);
    g2.dispose();
  }

  /**
   * for drawing rotated text
   * see
   * https://stackoverflow.com/questions/10083913/how-to-rotate-text-with-graphics2d-in-java
   */
  private void rotateText(String text, Point p1, Point p2, Graphics g) {
    if (text == null) {
      return;
    }
    double angle = Math.atan((p2.y - p1.y) / (double) (p2.x - p1.x));
    if (angle == -Math.PI / 2) {
      angle = angle + Math.PI;
    }
    // System.out.println(angle);
    Graphics2D g2d = (Graphics2D) g.create();
    Font font = new Font(null, Font.PLAIN, 12);

    Rectangle2D sbound = g.getFontMetrics().getStringBounds(text, g);
    int descent = g.getFontMetrics().getDescent();
    int ascent = g.getFontMetrics().getAscent();
    AffineTransform affineTransform = new AffineTransform();
    affineTransform.rotate(angle, 0, 0);
    // affineTransform.rotate(Math.toRadians(angle), 0, 0);
    affineTransform.translate(-sbound.getWidth() / 2, -2);

    Font rotatedFont = font.deriveFont(affineTransform);
    g2d.setFont(rotatedFont);
    g2d.drawString(text, (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    g2d.dispose();
    // System.out.println("Edge: "+text);
  }

  /** Draws the graph in a window */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    // System.out.println("Entering paintComponent.");

    // get node collection (varies depending on graph type)
    Set nodes = getNodeSet();
    // System.out.println("Nodes: "+nodes);

    // draw edges
    Graphics2D g2 = (Graphics2D) g.create();
    for (Object n : nodes) {
      Point loc = getLoc(n);
      for (Object e : getAdjacentNodes(n)) {
        Point dloc = getLoc(e);
        Object edge = getEdgeBetween(n, e);
        g2.setColor(getColor(edge));
        g2.setStroke(new BasicStroke(2));
        // System.out.println(getColor(edge)+" "+edge);
        if (isDirected) {
          drawArrow(loc, dloc, g2);
        } else {
          // Graphics2D g2 = (Graphics2D) g.create();
          g2.drawLine(loc.x, loc.y, dloc.x, dloc.y);
        }

        // add text
        rotateText(getLabel(edge), loc, dloc, g);
      }
    }
    g2.dispose();

    // draw nodes
    for (Object n : nodes) {
      Point pos = getLoc(n);
      g.setColor(getColor(n));
      g.fillOval(pos.x - NODE_RADIUS, pos.y - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
      g.setColor(Color.black);
      g.drawOval(pos.x - NODE_RADIUS, pos.y - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
      String label = getLabel(n);
      Rectangle2D sbound = g.getFontMetrics().getStringBounds(label, g);
      int descent = g.getFontMetrics().getDescent();
      int ascent = g.getFontMetrics().getAscent();
      g.setColor(Color.BLACK);
      g.drawString(label, pos.x - (int) sbound.getWidth() / 2 + labelOffset.x,
          pos.y + (int) (sbound.getHeight()) / 2 - descent + labelOffset.y);
      String note = getNote(n);
      Rectangle2D nbound = g.getFontMetrics().getStringBounds(note, g);
      descent = g.getFontMetrics().getDescent();
      ascent = g.getFontMetrics().getAscent();
      g.setColor(Color.RED.darker());
      g.drawString(note, pos.x - (int) nbound.getWidth() / 2 + noteOffset.x,
          pos.y + NODE_RADIUS + (int) (nbound.getHeight()) - descent + noteOffset.y);
    }
  }

  /** Timer callback causes the window to be repainted */
  public void actionPerformed(ActionEvent e) {
    repaint();
  }

  /** listener class for drag events */
  private class DragListener extends MouseAdapter {
    /** mouse click event handler */
    public void mouseClicked(MouseEvent e) {
      Object obj = getNode(e.getX(), e.getY());
      ActionListener callback = getCallback(obj);
      if (callback != null) {
        callback.actionPerformed(new ActionEvent(obj, e.getID(), "Click"));
      }
    }

    /** mouse press event handler */
    public void mousePressed(MouseEvent e) {
      dragPoint = new Point(e.getX(), e.getY());
      // System.out.println("Point set: "+dragPoint);
    }

    /** Release event handler */
    public void mouseReleased(MouseEvent e) {
      // System.out.println("Point cleared. ");
      dragPoint = null;
      activeNode = null;
    }

    /** event handler for drag events */
    public void mouseDragged(MouseEvent e) {
      // System.out.println("p: "+dragPoint+" node: "+activeNode);
      if (activeNode == null) {
        // System.out.println("Getting node");
        activeNode = getNode(dragPoint.x, dragPoint.y);
      }
      Point loc = getLoc(activeNode);
      loc.x = e.getX();
      loc.y = e.getY();
      // System.out.println("loc: "+loc+" "+activeNode);
      loc = getLoc(activeNode);
      // System.out.println("loc: "+getLoc(activeNode));
      repaint();
    }
  }

  /** class to represent an ordered pair for a directed edge */
  public class Pair<T, U> {
    /** one end of the pair */
    private final T p1;
    /** the other end of the pair */
    private final U p2;

    /** constructor for pair */
    public Pair(T p1, U p2) {
      this.p1 = p1;
      this.p2 = p2;
    }

    // from https://www.sitepoint.com/implement-javas-equals-method-correctly/
    /** equal if ends are equal */
    @Override
    public boolean equals(Object o) {
      if (this == o) // self check
        return true;
      if (o == null) // null check
        return false;
      if (getClass() != o.getClass()) // type check and cast
        return false;
      Pair p = (Pair) o;
      return Objects.equals(p1, p.p1) && Objects.equals(p2, p.p2); // field comparison
    }

    // from https://www.sitepoint.com/how-to-implement-javas-hashcode-correctly/
    /** both endpoints contribute to hashcode */
    @Override
    public int hashCode() {
      return Objects.hash(p1, p2);
    }

    /** return an appropriate default string */
    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
      String result;
      if ((vgraph != null) && vgraph.hasEdgeConnecting(p1, p2)) {
        // System.out.println(vgraph.edgeValue(p1,p2));
        result = vgraph.edgeValue(p1, p2).get().toString();
      } else {
        result = "";
      }
      return result;
    }
  }

  /** class to represent a diset for an undirected edge */
  public class Diset<T> {
    /** one end of the pair */
    private final T p1;
    /** the other end of the pair */
    private final T p2;

    /** constructor for pair */
    public Diset(T p1, T p2) {
      this.p1 = p1;
      this.p2 = p2;
    }

    // from https://www.sitepoint.com/implement-javas-equals-method-correctly/
    /** equal if ends are equal */
    @Override
    public boolean equals(Object o) {
      if (this == o) // self check
        return true;
      if (o == null) // null check
        return false;
      if (getClass() != o.getClass()) // type check and cast
        return false;
      Diset p = (Diset) o;
      return (Objects.equals(p1, p.p1) && Objects.equals(p2, p.p2))
          || (Objects.equals(p2, p.p1) && Objects.equals(p1, p.p2)); // field comparison
    }

    // from https://www.sitepoint.com/how-to-implement-javas-hashcode-correctly/
    /** both endpoints contribute to hashcode */
    @Override
    public int hashCode() {
      return Objects.hash(p1) + Objects.hash(p2);
    }

    /** return an appropriate default string */
    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
      String result;
      if ((vgraph != null) && vgraph.hasEdgeConnecting(p1, p2)) {
        // System.out.println(vgraph.edgeValue(p1,p2));
        result = vgraph.edgeValue(p1, p2).get().toString();
      } else {
        result = "";
      }
      return result;
    }
  }
}
