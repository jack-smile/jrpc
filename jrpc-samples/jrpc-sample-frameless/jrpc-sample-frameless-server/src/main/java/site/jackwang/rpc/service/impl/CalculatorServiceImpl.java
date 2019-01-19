package site.jackwang.rpc.service.impl;


import site.jackwang.rpc.service.CalculatorService;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/17
 */
public class CalculatorServiceImpl implements CalculatorService {
    @Override
    public double add(double num1, double num2) {
        return num1 + num2;
    }

    @Override
    public double substract(double num1, double num2) {
        return num1 - num2;
    }
}
