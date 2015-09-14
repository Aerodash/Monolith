package com.aerodash.monolith.djikstra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aerodash.monolith.main.Monolith;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Graph {
	private List<Vertex> nodes;
	private List<Edge> edges;

	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.nodes = vertexes;
		this.edges = edges;
	}

	public Graph() {
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	}

	public List<Vertex> getNodes() {
		return nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void render(ShapeRenderer sr) {
		if (!nodes.isEmpty()) {
			sr.begin(ShapeType.Line);
			sr.setColor(Color.RED);
			for (Vertex v : nodes) {
				sr.circle(v.getTile().getX() + Monolith.tileSize / 2, v.getTile().getY() + Monolith.tileSize / 2, Monolith.tileSize / 2);
			}
			sr.end();
		}
	}

	public void addNode(Vertex v) {
		nodes.add(v);
	}
	
	public Vertex getNodeById(String id){
		Iterator<Vertex> it = nodes.iterator();
		while(it.hasNext()){
			Vertex v = it.next();
			if (v.getId().equals(id)) return v;
		}
		return null;
	}

	public void addEdge(String laneId, int sourceLocNo, int destLocNo, int duration) {
		if (!nodes.isEmpty()){
			Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
			edges.add(lane);
		}
	}

}
