package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl38
 * @date 01/01/2024
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }

    @Override
    public boolean equals(Object sig){
        if (!(sig instanceof Signature)){
            return false;
        }
        Signature sigToCompare = (Signature) sig;
        if(this.args.size() != sigToCompare.size()) {
            return false;
        }
        for (int i = 0; i < sigToCompare.args.size(); i++){
            if (this.paramNumber(i) != sigToCompare.paramNumber(i)){
                return false;
            }
        }
        return true;
    }


    public List<Type> getList() {return args;}

}
