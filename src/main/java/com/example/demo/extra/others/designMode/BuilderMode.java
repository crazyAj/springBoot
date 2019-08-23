package com.example.demo.extra.others.designMode;

import lombok.Data;

/**
 * 建造者模式
 */
public class BuilderMode {

    public static void main(String[] args) {
        Builder builder = new ConstructBuilder();
        builder.builderA("aaa");
        builder.builderB("bbb");
        System.out.println("建造者模式： " + builder.build());
    }

}

@Data
class MyBuilder {
    private String buildA;
    private String buildB;
}

interface Builder {
    Builder builderA(String a);

    Builder builderB(String b);

    MyBuilder build();
}

class ConstructBuilder implements Builder {
    private MyBuilder myBuilder;

    public ConstructBuilder() {
        myBuilder = new MyBuilder();
    }

    @Override
    public Builder builderA(String a) {
        myBuilder.setBuildA(a);
        return this;
    }

    @Override
    public Builder builderB(String b) {
        myBuilder.setBuildB(b);
        return this;
    }

    @Override
    public MyBuilder build() {
        return myBuilder;
    }
}