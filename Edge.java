/**
 * The {@code Edge} record class represents the edge between `source` cell and `dest` cell. `source` and `dest` are
 * the ID of the {@code Cell}.
 */
public record Edge(int source, int dest) {
	public int getSource() {
		return this.source;
	}

	public int getDest() {
		return this.dest;
	}
}
