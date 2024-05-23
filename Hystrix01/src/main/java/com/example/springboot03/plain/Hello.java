package com.example.springboot03.plain;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

import java.util.function.Supplier;

// 예제3 : Supplier 인터페이스 (함수형 인터페이스)
public class Hello {
    public static void main(String[] args) {
        A a1 = new A() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        A a2 = () ->{
            return "Tiger";
        };
        A a3 = () -> 3.14f;

        B b = new B(); // ::문법을 사용하기 위해 객체 생성

        // 만들어져 있는 인터페이스 사용하기
        Supplier<Integer> a4 = () -> 10;
        Supplier a41 = () -> {
            b.f1();
            return 3.14f;
        };
        Supplier<Integer> a5 = b::f1;

        // resilience4j 예제코드
        Supplier<Integer> supplier =
                CircuitBreaker.decorateSupplier(circuitBreaker, resQuoteService::getQuote); // 위 예제들로 봤을 때 이제 이 코드를 이해할 수 있다.
//        ()->{return resQuoteService.getQuote();} // 람다 원형
        // new Supplier (){return resQuoteService.getQuote();} // 원형
    }
}
interface A<T>{
    //Supplier 인터페이스와 동일
    T get();
}
class B{
    int f1() {
        return 1000;
    }
}
//// 예제 1,2번
//public class Hello {
//    public static void main(String[] args) {
//        C obj = new C();
//
//        // 1-1
//        A a1= new A() {
//            @Override
//            public int f1() {
//                return 1;
//            }
//        };
//        a1.f1();
//        System.out.println(a1.f1());
//
//        // 1-2 (약식 방법)
//        A a2 = () -> {
//            return 2;
//        };
//        a2.f1();
//        System.out.println(a2.f1());
//
//        // 1-3 (더 약식)
//        A a3 = () -> 3;
//
////        A a4 = obj::f3; // 인터페이스 리턴타입 바뀌면서 에러
//
//        B b1 = new B() {
//            @Override
//            public void f2(int num) {
//                System.out.println(num);
//            }
//        };
//        b1.f2(10);
//
//        B b2 = (num) ->{
//            obj.f4(num);
//        };
//        b2.f2(10);
//
//        // 람다 함수 대신 ::을 사용 (메소드 참조 표현식)
//        // http://yoonbumtae.com/?p=2776
//        B b3 = obj::f4;;
//
//        D d3 = obj::f5;
//        d3.f3(99, "tiger");
//
//    }
//}
//interface A{
//    int  f1();
//}
//interface B{
//    void  f2(int num);
//}
//class C{
//    void f3(){
//        System.out.println("Test3");
//    }
//
//    void f4(int num){
//        System.out.println("Test4");
//    }
//    void  f5(int num,String str){
//        System.out.println("Test5");
//    }
//
//}
//interface D{
//    void  f3(int num,String str);
//}
//
