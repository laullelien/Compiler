package fr.ensimag.deca.tree;

public class MethodAsmBody extends AbstractMethodBody {
    private StringLiteral assemblyCode;

    public MethodAsmBody(StringLiteral assemblyCode) {
        this.assemblyCode = assemblyCode;
    }
}
