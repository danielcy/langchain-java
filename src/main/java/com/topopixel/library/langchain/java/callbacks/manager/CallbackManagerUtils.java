package com.topopixel.library.langchain.java.callbacks.manager;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import lombok.var;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CallbackManagerUtils {

    static <T> T configure(Class<T> clazz, List<BaseCallbackHandler> inheritableCallbacks,
        List<BaseCallbackHandler> localCallbacks) {
        return configure(clazz, inheritableCallbacks, localCallbacks, false);
    }

    static <T> T configure(Class<T> clazz, List<BaseCallbackHandler> inheritableCallbacks,
        List<BaseCallbackHandler> localCallbacks, boolean verbose) {
        try {
            T callbackManager = clazz.newInstance();
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

            // TODO: verbose

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
                Method event = handler.getClass().getMethod(eventName);
                event.invoke(args);
            } catch (NoSuchMethodException e) {
                //TODO: on chat model start
            } catch (Exception e) {
                // TODO: exception
            }
        }
    }
}
