package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.util.HashMap;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl38
 * @date 01/01/2024
 */
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).
    private HashMap<Symbol, ExpDefinition> environment = new HashMap<>();

    EnvironmentExp parentEnvironment;

    public EnvironmentExp getParentEnvironment() {
        return parentEnvironment;
    }
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
    }

    public EnvironmentExp() {
        this.parentEnvironment = null;
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        return environment.get(key);
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        if (this.environment.containsKey(name)) {
            throw new DoubleDefException();
        }
        this.environment.put(name, def);
    }

    public HashMap<Symbol, ExpDefinition> getEnvironment(){
        return this.environment;
    }

    /**
     * Stack two environment with env1 having the priority.
     * If env2 is null, an environment with the definitions from env1 is returned.
     *
     * @param env1
     *          the environment stacked on env2
     * @param env2
     *          the environment with env1 stacked onto it
     *
    */
    public EnvironmentExp stackEnvironment(EnvironmentExp env1, EnvironmentExp env2) {
        EnvironmentExp res = new EnvironmentExp(null);
        if (env2 == null) {
            res.environment.putAll(env1.environment);
        } else {
            res.environment.putAll(env2.environment);
            for (Symbol name : env1.environment.keySet()) {
                res.environment.put(name, env1.environment.get(name));
            }
        }
        return res;
    }

    public void stackEnvironment(EnvironmentExp env) {
        for (Symbol s : env.getEnvironment().keySet()){
            this.environment.put(s, env.get(s));
        }
    }

    public void declare(EnvironmentExp env) throws DoubleDefException {
        for (Symbol s : env.getEnvironment().keySet()){
            this.declare(s, env.get(s));
        }
    }



}
