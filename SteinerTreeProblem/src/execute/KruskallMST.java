package execute;

import java.io.IOException;

import input.STPConfig;
import kruskall.Kruskall;
import visual.DrawConfig;
import visual.TreeDrawer;
import disjointset.Node;
import basic.Point;
import basic.Tree;

public class KruskallMST {

	static STPConfig stpcfg = new STPConfig(
			100,		// Nb of points,
			6,		// index of data
			0		// Nb of Steiner points
			);
	static DrawConfig drawcfg = new DrawConfig(
			600,	// width
			600,	// length
			2	// pointsize
			);
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		Node[] nodes = Kruskall.getNodes(stpcfg.getSTP().getPoints());
		Kruskall krus = new Kruskall(nodes);
		krus.execute();
		Tree tree = krus.getMST();
		TreeDrawer td = new TreeDrawer(tree,drawcfg.pointsize);
		td.draw(drawcfg.width, drawcfg.length);

	}

}
