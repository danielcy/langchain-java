package com.topopixel.library.langchain.java.callbacks.manager;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import com.topopixel.library.langchain.java.callbacks.stdout.StdOutCallbackHandler;
import com.topopixel.library.langchain.java.exception.NotImplementedException;
import com.topopixel.library.langchain.java.schema.BaseMessage;
import com.topopixel.library.langchain.java.schema.SchemaUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import lombok.var;

public class CallbackManagerUtils {

    static <T extends CallbackManager> T configure(Class<T> clazz, List<BaseCallbackHandler> inheritableCallbacks,
        List<BaseCallbackHandler> localCallbacks) {
        return configure(clazz, inheritableCallbacks, localCallbacks, false);
    }

    static <T extends CallbackManager> T configure(Class<T> clazz, List<BaseCallbackHandler> inheritableCallbacks,
        List<BaseCallbackHandler> localCallbacks, boolean verbose) {
        try {
            Constructor<T> defaultConst = clazz.getConstructor(List.class);
            T callbackManager = defaultConst.newInstance(new ArrayList<>());
            if (inheritableCallbacks != null || localCallbacks != null) {
                List<BaseCallbackHandler> inheritableCallbacks_ = inheritableCallbacks != null ? inheritableCallbacks : new ArrayList<>();
                Constructor<T> constructor = clazz.getConstructor(List.class, List.class);
                callbackManager = constructor.newInstance(inheritableCallbacks_, inheritableCallbacks_);

                List<BaseCallbackHandler> localCallbacks_ = localCallbacks != null ? localCallbacks : new ArrayList<>();
                Method mAddHandler = clazz.getMethod("addHandler", BaseCallbackHandler.class);
                for (var handler: localCallbacks_) {
                    mAddHandler.invoke(callbackManager, handler);
                }
            }

            // TODO: tracer

            // TODO: some other condition
            if (verbose) {
                if (verbose && callbackManager.getHandlers().stream()
                .noneMatch(h -> h instanceof StdOutCallbackHandler)) {
                    // TODO: debug
                    callbackManager.addHandler(StdOutCallbackHandler.stdOutBuilder().build(), false);
                }
            }

            return callbackManager;
        } catch (Exception e) {
            // TODO: handle exceptions
            return null;
        }

    }

    static void handleEvent(List<BaseCallbackHandler> handlers, String eventName,
        String ignoreConditionName, Object...args) {
        List<String> messageStrings = null;
        for (var handler: handlers) {
            try {
                if (ignoreConditionName != null) {
                    try {
                        Method mIgnore = handler.getClass().getMethod(ignoreConditionName);
                        Boolean result = (Boolean) mIgnore.invoke(handler);
                        if (result) {
                            continue;
                        }
                    } catch (NoSuchMethodException e) {
                        // no such ignore method
                    }
                }
                Method[] methods = handler.getClass().getMethods();
                Method event = null;
                for (Method method: methods) {
                    if (eventName.equals(method.getName())) {
                        event = method;
                    }
                }
                if (event != null) {
                    event.invoke(handler, args);
                }
            } catch (NotImplementedException e) {
                if ("onChatModelStart".equals(eventName)) {
                    if (messageStrings == null) {
                        List<List<BaseMessage>> msgArg = (List<List<BaseMessage>>)args[1];
                        messageStrings = msgArg.stream()
                            .map(SchemaUtils::getBufferString)
                            .collect(Collectors.toList());
                    }
                    List<Object> newArgs = new ArrayList<>();
                    if (args.length > 2) {
                        for (int i = 2; i < args.length; i++) {
                            newArgs.add(args[i]);
                        }
                    }
                    handleEvent(Arrays.asList(handler), "onLLMStart",
                        "isIgnoreLLM", args[0], messageStrings,
                        newArgs.toArray());

                }
            } catch (Exception e) {
                // TODO: exception
                e.printStackTrace();
            }
        }
    }
}
