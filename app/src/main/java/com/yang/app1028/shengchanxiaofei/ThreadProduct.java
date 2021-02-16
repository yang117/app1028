package com.yang.app1028.shengchanxiaofei;

public class ThreadProduct extends Thread {
    private Product product;

    public ThreadProduct(Product product) {
        this.product = product;
    }

    @Override
    public void run() {
        while (true) {
            product.setValue(); //死循环，不断生产
        }
    }
}
