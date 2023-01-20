public class Drink {

	private String name;
	private int price;
	private int stock;
	private String drinkCode;

	public Drink(String name, int price, int stock, String drinkCode) {
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.drinkCode = drinkCode;
	}

	public String getName() {
		// TODO implement here
		return this.name;
	}

	public int getPrice() {
		return this.price;
	}
	public int getStock() {
		return this.stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getDrinkCode() {
		return this.drinkCode;
	}

}