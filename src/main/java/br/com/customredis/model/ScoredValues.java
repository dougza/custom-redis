package br.com.customredis.model;

public class ScoredValues<T extends Comparable<T>>  implements Comparable<ScoredValues<T>> {

	public int score;
	public T value;
	
	public ScoredValues(int score, T value) {
		this.score = score;
		this.value = value;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	@Override
	public int compareTo(ScoredValues<T> item) {
	    int cmp = -Double.compare(score, item.score);
	    if (cmp == 0) {
	        return value.toString().getBytes().toString().compareTo(item.value.toString().getBytes().toString());
	    }
	    return cmp;
	}

	
	@Override
    public String toString() {
        return "Scored{" +
                "score: " + score +
                ", value: '" + value + '\'' +
                '}';
    }
}
