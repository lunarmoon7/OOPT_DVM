
public abstract class AbstractDVMClass
{
    protected abstract void calcClosestDVMLoc();
    public void templateCalcClosestDVMLoc()
    {
        calcClosestDVMLoc();
    }
    protected abstract boolean checkOurDVMStock(String drinkCode, int drinkNum);
    public void templateCheckOurDVMStock(String drinkCode, int drinkNum)
    {
        checkOurDVMStock(drinkCode, drinkNum);
    }
    protected abstract void start();
    public void templateStart()
    {
        start();
    }
}