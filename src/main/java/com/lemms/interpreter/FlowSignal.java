package com.lemms.interpreter;

import com.lemms.interpreter.object.LemmsData;

public class FlowSignal {
  public final SignalType signal;
  public final LemmsData value; // only meaningful when signal==RETURN

  public static FlowSignal NORMAL = new FlowSignal(SignalType.NORMAL, null);
  
  public enum SignalType {
    NORMAL, RETURN, BREAK, CONTINUE
  }

  private FlowSignal(SignalType s, LemmsData v) {
    this.signal = s;
    this.value = v;
  }

  public static FlowSignal returned(LemmsData v) {
    return new FlowSignal(SignalType.RETURN, v);
  }

  public static FlowSignal broke() {
    return new FlowSignal(SignalType.BREAK, null);
  }

  public static FlowSignal continued() {
    return new FlowSignal(SignalType.CONTINUE, null);
  }
}
