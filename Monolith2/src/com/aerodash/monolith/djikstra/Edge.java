package com.aerodash.monolith.djikstra;

import java.util.ArrayList;
import java.util.List;

import com.aerodash.monolith.core.Tile;

public class Edge {

	private ArrayList<Tile> tiles;
	private final String id;
	private final Vertex source;
	private final Vertex destination;
	private int weight;

	public Edge(String id, Vertex source, Vertex destination, int weight) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
		tiles = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public Vertex getDestination() {
		return destination;
	}

	public Vertex getSource() {
		return source;
	}

	public int getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return source + " " + destination;
	}

	public void addTile(Tile t){
		tiles.add(t);
	}
	
	public void addTiles(List<Tile> t){
		tiles.addAll(t);
	}
}