import GsonConverter.Deserializer;
import GsonConverter.Serializer;
import Model.Message;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Dvmmain {
    public static void main(String[] args) {
        AbstractDVMClass dvm = DVM.getInstance();
        dvm.templateStart();
    }
}