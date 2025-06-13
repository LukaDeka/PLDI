package com.lemms.interpreter;
public class FlowSignal {
  public final SignalType signal;
  public final Object value; // only meaningful when signal==RETURN

  public static FlowSignal NORMAL = new FlowSignal(SignalType.NORMAL, null);
  
  public enum SignalType {
    NORMAL, RETURN, BREAK, CONTINUE
  }

  private FlowSignal(SignalType s, Object v) {
    this.signal = s;
    this.value = v;
  }

  public static FlowSignal returned(Object v) {
    return new FlowSignal(SignalType.RETURN, v);
  }

  public static FlowSignal broke() {
    return new FlowSignal(SignalType.BREAK, null);
  }

  public static FlowSignal continued() {
    return new FlowSignal(SignalType.CONTINUE, null);
  }
}
