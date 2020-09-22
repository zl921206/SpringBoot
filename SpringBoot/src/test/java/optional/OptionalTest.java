package optional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * java8 optional test
 */
public class OptionalTest {

    public static void main(String[] args) {
        /**
         * 创建
         */
        // 创建一个空对象
        Optional<String> optional = Optional.empty();
        // 判断不为空
        System.out.println(optional.isPresent());
        // 创建一个字符串对象(不能传空值，否则抛异常)
        Optional<String> optStr1 = Optional.of("admin");
        System.out.println(optStr1.toString());
        // 创建一个字符串对象，可以传空值
        Optional<String> optStr2 = Optional.ofNullable(null);
        System.out.println(optStr2.toString());

        /**
         * 检查：不为空返回true；为空返回false
         */
        System.out.println(optStr1.isPresent());
        System.out.println(optStr2.isPresent());
        optStr1.ifPresent(name -> System.out.println(name.length()));

        /**
         * orElse(): 检索Optional对象中的值，它被传入一个“默认参数‘。如果对象中存在一个值，则返回它，否则返回传入的“默认参数”
         */
        String value = "abc";   // 当value不为null时，返回value值，并且继续执行orElse()中的getVal()方法；为null时仅执行orElse()中的getVal()方法
        String name1 = Optional.ofNullable(value).orElse(getVal());
        System.out.println(name1);

        /**
         * orElseGet(): 与orElse类似，但是这个函数不接收一个“默认参数”，而是一个函数接口
         * 区别：
         *  1：orElse(): 当value不为null时，返回value值，并且继续执行orElse()中的getVal()方法；为null时仅执行orElse()中的getVal()方法
         *  2：orElseGet(): 当value不为null时，返回value值，orElseGet()中的getVal()方法不执行；为null时仅执行orElseGet()中的getVal()方法
         * 总结：当值存在时，orElse相比于orElseGet，多创建了一个对象
         */
        value = null;   // 当value不为null时，返回value值，orElseGet()中的getVal()方法不执行；为null时仅执行orElseGet()中的getVal()方法
        String name2 = Optional.ofNullable(value).orElseGet(OptionalTest::getVal);
        System.out.println(name2);

        /**
         * orElseThrow当遇到一个不存在的值的时候，并不返回一个默认值，而是抛出异常
         */
        String name3 = "defaultVal";
        try {
            name3 = Optional.ofNullable(value).orElseThrow(Exception::new);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("执行了orElseThrow()......");
        }
        System.out.println(name3);

        /**
         * get(): 使用该方法时，值必须存在，负责会抛出一个NoSuchElementException异常
         */
        System.out.println(optStr1.get());
//        System.out.println(optStr2.get());

        /**
         * filter()：接收一个函数式接口，当符合接口时，则返回一个Optional对象，否则返回一个空的Optional对象
         */
        boolean signStr = optStr1.filter(val -> val == optStr1.get()).isPresent();
        System.out.println(signStr);

        /**
         * map()：将一个值转换为另一个值
         */
        List<String> list = Arrays.asList("apple", "huawei", "sanxing", "xiaomi");
        Optional<List<String>> opts = Optional.of(list);
        int size = opts.map(List::size).orElse(0);
        System.out.println(size);
        int len = optStr1.map(String::length).orElse(0);
        System.out.println(len);

        /**
         * filter() 整合 map()
         *  校验密码
         */
        String password = " password ";
        Optional<String> passOpt = Optional.of(password);
        boolean verifyResult = passOpt.filter(pass -> pass.equals("password")).isPresent();
        System.out.println(verifyResult);
        verifyResult = passOpt.map(String::trim).filter(pass -> pass.equals("password")).isPresent();
        System.out.println(verifyResult);

        /**
         * flatmap()：map()只有当值不被包裹时才进行转换，而flatmap()接受一个被包裹着的值并且在转换之前对其解包
         */
        Person person = new Person("zhangsan", 26);
        Optional<Person> personOptional = Optional.of(person);
        // 使用map()
        Optional<Optional<String>> nameOptionalWrapper = personOptional.map(Person::getName);
        Optional<String> nameOptional = nameOptionalWrapper.orElseThrow(IllegalArgumentException::new);
        String name = nameOptional.orElse("lisi");
        System.out.println(name);
        Optional<String> str = personOptional.map(Person::getName).orElse(Optional.ofNullable("wangmazi"));
        name = str.orElseGet(() -> "zhaoliu");
        System.out.println(name);
        // 使用flatmap()
        person.setName(null);
        name = personOptional.flatMap(Person::getName).orElse("wangwu");
        System.out.println(name);
    }

    /**
     * 测试方法
     * @return
     */
    static String getVal(){
        System.out.println("执行输出：getVal() 方法");
        return "123";
    }
}
