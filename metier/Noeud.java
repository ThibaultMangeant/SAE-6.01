class Noeud {
    int id;
    double x, y;
    int demande;

    public Noeud(int id, double x, double y, int demande) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.demande = demande;
    }

	public double distance(Noeud autre) {
		return Math.sqrt(Math.pow(this.x - autre.x, 2) + Math.pow(this.y - autre.y, 2));
	}
}