addressCut2
===========
<a href="https://github.com/naturemickey/addressCut" target="_blank">addressCut</a>�ĵڶ����汾��
���һ���汾����DFA�����˲�ͬ���㷨��

ʹ��ʾ����

public class Test {

	public static void main(String[] args) {
		String addr = "�㶫���ڱ���ɳ��";
		Address address = AddressScanner.scan(addr);
		System.out.println(address.getProvinceAddress());
		System.out.println(address.getCityAddress());
		System.out.println(address.getAreaAddress());
		System.out.println(address.getTownAddress());
		System.out.println(address.getDetailAddress());
	}

}
