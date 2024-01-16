package fr.ensimag.deca.tree;

public class MethodBody extends AbstractMethodBody {
    private ListDeclVar declVar;
    private ListInst inst;

    public MethodBody(ListDeclVar declVar, ListInst inst) {
        this.declVar = declVar;
        this.inst = inst;
    }
}
