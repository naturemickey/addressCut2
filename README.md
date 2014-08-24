addressCut2
===========
<a href="https://github.com/naturemickey/addressCut" target="_blank">addressCut</a>的第二个版本。
与第一个版本构造DFA采用了不同的算法。

使用示例：

public class Test {

	public static void main(String[] args) {
		String addr = "广东深圳宝安沙井";
		Address address = AddressScanner.scan(addr);
		System.out.println(address.getProvinceAddress());
		System.out.println(address.getCityAddress());
		System.out.println(address.getAreaAddress());
		System.out.println(address.getTownAddress());
		System.out.println(address.getDetailAddress());
	}

}
