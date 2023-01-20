public class Card {

	public int balance;
	public Card(int balance) {
		this.balance = balance;
	}

	public int getBalance() {
		// TODO implement here
		return this.balance;
	}

	public void setBalance(int balance) {
		// TODO implement here
		this.balance -= balance;
	}
}