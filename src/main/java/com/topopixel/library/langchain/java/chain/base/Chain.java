package com.topopixel.library.langchain.java.chain.base;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import com.topopixel.library.langchain.java.callbacks.manager.*;
import com.topopixel.library.langchain.java.exception.NotImplementedException;
import java.util.*;
import lombok.var;

/**
 * """Base interface that all chains should implement."""
 */
public abstract class Chain {

    // TODO: memory
    protected List<BaseCallbackHandler> callbacks;
    protected boolean verbose = false;

    private void getChainType() {
        throw new NotImplementedException("Saving not supported for this chain type.");
    }

    public abstract List<String> getInputKeys();

    public abstract List<String> getOutputKeys();

    // TODO: input/output validate

    public abstract Map<String, Object> internalCall(Map<String, Object> inputs,
        CallbackManagerForChainRun runManager);

    public Map<String, Object> call(Map<String, Object> inputs) {
        return call(inputs, false, null);
    }

    public Map<String, Object> call(Map<String, Object> inputs, boolean returnOnlyOutput) {
        return call(inputs, returnOnlyOutput, null);
    }

    public Map<String, Object> call(Map<String, Object> inputs,
        List<BaseCallbackHandler> callbacks) {
        return call(inputs, false, callbacks);
    }

    public Map<String, Object> call(Map<String, Object> inputs, boolean returnOnlyOutput,
        List<BaseCallbackHandler> callbacks) {

        var realInputs = prepInput(inputs);
        var callbackManager = CallbackManager.configure(callbacks, this.callbacks,
            verbose);
        var runManager = callbackManager.onChainStart(new HashMap<String, Object>() {{
            put("name", getClass().getName());
        }}, inputs, null);
        Map<String, Object> outputs = new HashMap<>();
        try {
            outputs = internalCall(inputs, runManager);
        } catch (Exception e) {
            runManager.onChainError(e);
            throw e;
        }
        runManager.onChainEnd(outputs);
        return prepOutput(realInputs, outputs, false);
    }

    public Map<String, Object> prepInput(Map<String, Object> inputs) {
        // TODO: memory
        // TODO: validate
        return inputs;
    }

    public Map<String, Object> prepOutput(Map<String, Object> inputs,
        Map<String, Object> outputs, boolean returnOnlyOutputs) {
        // TODO: validate

        // TODO: memory
        if (returnOnlyOutputs) {
            return outputs;
        }
        Map<String, Object> result = new HashMap<>();
        result.putAll(inputs);
        result.putAll(outputs);
        return result;
    }

    public String run(Map<String, Object> args) {
        return run(args, null);
    }

    public String run(Map<String, Object> args, List<BaseCallbackHandler> callbacks) {
        return String.valueOf(call(args, callbacks).get(getOutputKeys().get(0)));
    }
}
