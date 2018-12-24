//package horeilly1101;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import com.sun.xml.internal.xsom.impl.scd.Step;
//import horeilly1101.Expression.*;
//
//import javax.swing.plaf.synth.SynthTextAreaUI;
//
//import static java.util.stream.Collectors.toList;
//import java.util.stream.*;
//
///**
// * This class centralizes all the logic that simplifies Expressions when
// * they're constructed. This lets us enforce a certain ordering on our
// * terms/factors, and it lets us predict the form of a given input.
// */
//class SimplifyExpression {
//  // -------------
//  // MULT
//  // -------------
//
//
//  // -------------
//  // ADD
//  // -------------

//
//  // -------------
//  // LOG
//  // -------------
//
////  /**
////   * Simplifies a logarithmic expression. Always returns a list of three
////   * Expression objects, where the first is a constant, the second is the base,
////   * and the third is the result.
////   */
////  static List<Expression> logSimplify(Expression base, Expression result) {
////    Constant con = Constant.multID();
////
////    // check if result is 1
////    if (result.equals(Constant.multID())) {
////      return Stream.of(Constant.addID(), base, result)
////                 .collect(toList());
////    }
////
////    List<Expression>
////
////    return Stream.of(base, exponent).collect(toList());
////  }
//
//  // -------------
//  // POWER
//  // -------------
//  static List<Expression> powerSimplify(Expression base, Expression exponent) {
//    // check if exponent is 1.0
//    if (exponent.equals(Expression.Constant.multID())) {
//      return Stream.of(base).collect(toList());
//    }
//
//    // check if power
//    if (base.getClass().equals(Constant.class) && exponent.getClass().equals(Constant.class)) {
//      return Stream.of(
//          Constant.constant(
//              Math.pow(
//                  base.asConstant().getVal(),
//                  exponent.asConstant().getVal())))
//                 .collect(toList());
//    }
//
//
//
//    return Stream.of(base, exponent).collect(toList());
//  }
//}
