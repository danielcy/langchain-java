package com.topopixel.library.langchain.java.callbacks.manager;

import com.topopixel.library.langchain.java.callbacks.base.BaseCallbackHandler;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import lombok.var;

public class CallbackManagerUtils {

    public static <T> T configure(Class<T> clazz, List<BaseCallbackHandler> inheritableCallbacks,
        List<BaseCallbackHandler> localCallbacks) {
        return configure(clazz, inheritableCallbacks, localCallbacks, false);
    }

    public static <T> T configure(Class<T> clazz, List<BaseCallbackHandler> inheritableCallbacks,
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
}
