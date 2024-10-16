package fr.ensimag.deca;

import fr.ensimag.deca.codegen.CodegenHelper;
import fr.ensimag.deca.codegen.ListVTable;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.extension.SSAFormHelper;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl38
 * @date 01/01/2024
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    /**
     * ID of the first available Register for code generation
     */
    private int regId = 2;
    private int maxReg = 2;

    private int regVar;

    /**
     * Represente le nombre maximal de variables affectees a des registres
     */
    public void setRegVar(int n) {
        regVar = n;
    }

    private int allocatedReg = 0;

    public DVal getVarDVal() {
        if(allocatedReg < regVar) {
            DVal reg = (Register.getR(compilerOptions.getMaxRegisters() - allocatedReg));
            allocatedReg ++;
            return reg;
        } else {
            nbDeclVar ++;
            return new RegisterOffset(listVTable.getOffset() + nbDeclVar, Register.GB);
        }
    }

    public void setRegister(GPRegister reg) {
        regId = reg.getNumber();
    }

    public GPRegister getRegister() {
        return Register.getR(regId);
    }

    public void incrementRegister() {
        regId++;
        if(regId > maxReg) {
            maxReg = regId;
        }
        Validate.isTrue(regId <= compilerOptions.getMaxRegisters());
    }

    public void decrementRegister() {
        regId--;
        Validate.isTrue(regId >= 2);
    }

    public void resetMaxReg() {
        maxReg = 2;
    }

    public int getMaxReg() {
        return maxReg;
    }

    public boolean isRegisterAvailable() {
        return regId < compilerOptions.getMaxRegisters();
    }

    /**
     * DVal used for binary operations, in code generation
     */

    private DVal dval;

    public DVal getDVal() {
        return dval;
    }

    public void setDval(DVal dval) {
        this.dval = dval;
    }

    private int nbDeclVar = 0;

    public int generateDeclVarOffset() {
        nbDeclVar ++;
        return listVTable.getOffset() + nbDeclVar;
    }

    public int getNbDeclVar() {
        return nbDeclVar;
    }

    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        lastInstruction = instruction;
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }

    private Instruction lastInstruction;

    public Instruction getLastInstruction() {
        return lastInstruction;
    }

    /**
     *
     * @return true if and only if the last instruction is a load instruction
     */
    public Boolean lastIsLoad() {
        return program.lastIsLoad();
    }

    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }

    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private IMAProgram program = new IMAProgram();

    public void setProgram(IMAProgram program) {
        this.program = program;
    }

    public IMAProgram getProgram() {
        return program;
    }

    /** The global environment for types (and the symbolTable) */
    public final SymbolTable symbolTable = new SymbolTable();

    public final EnvironmentType environmentType = new EnvironmentType(this);

    /** The list of VTable for Etape C codegen */
    public final ListVTable listVTable = new ListVTable();

    /** Helper class for code generation */
    public final CodegenHelper codegenHelper = new CodegenHelper(this);

    /** Helper class for SSA-CFG */
    public final SSAFormHelper ssaFormHelper = new SSAFormHelper();

    public Symbol createSymbol(String name) {
        return symbolTable.create(name);
    }

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = sourceFile.substring(0, sourceFile.length() - 4);
        destFile += "ass";
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());

        if (this.compilerOptions.getParse()){
            prog.decompile(out);
            return false;
        }

        prog.verifyProgram(this);
        assert(prog.checkAllDecorations());

        if (this.compilerOptions.getVerification()){
            return false;
        }

        if (this.compilerOptions.getOptim()) {
            prog.optimizeProgram(this);
        }

        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");

        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

    /*
     * Used to get different labelId
     */
    private int labelId = 0;

    public int getLabelId() {
        return labelId;
    }

    public void incrementLabelId() {
        labelId++;
    }
}
