//package com.deriv.util;
//
//import org.junit.jupiter.api.Test;
//import com.deriv.expression.Expression;
//
//import static com.deriv.expression.Constant.*;
//
//public class MatrixTest {
//  @Test
//  void matrixTest() {
//    Expression[][] arr = new Expression[][] {{ constant(2), multID() }, { constant(4), constant(3) }};
//    Matrix<Expression> mat = Matrix.make(arr);
//    System.out.println(mat);
//
//    Expression[][] arr2 = new Expression[][] {{ multID()}, { multID() }};
//    Matrix<Expression> mat2 = Matrix.make(arr2);
//    System.out.println(mat2);
//
//    System.out.println(mat.times(mat2));
//  }
//}
