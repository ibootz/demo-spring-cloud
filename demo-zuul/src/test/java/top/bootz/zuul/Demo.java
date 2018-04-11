package top.bootz.zuul;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

public class Demo {

	public static void main(String[] args) {
		Demo demo = new Demo();
		List<Question> questions = demo.builderQuestion();
		String userExamId = "d9b6d35a-b8d1-4aff-bd65-bd23b22e9c46";

		long start = System.currentTimeMillis();
		random(questions, userExamId, 3);
		System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");

		// long start = System.currentTimeMillis();
		// random2(questions, userExamId, 1);
		// System.out.println("耗时：" + (System.currentTimeMillis() - start) +
		// "ms");

		// long start = System.currentTimeMillis();
		// random3(questions, userExamId, 1);
		// System.out.println("耗时：" + (System.currentTimeMillis() - start) +
		// "ms");
	}

	private static void random3(List<Question> questions, String userExamId, int repeatTimes) {
		String md5 = md5(userExamId + repeatTimes);
		md5 += md5(md5);
		String numStr = getNumMd5(md5);
		numStr = StringUtils.reverse(numStr);

		System.out.println(numStr);

		Question question = null;
		List<Option> opts = null;
		Option opt = null;
		for (int i = 0; i < questions.size(); i++) {
			question = questions.get(i);
			question.setOrderIndex(numStr.substring(i, i + 2));
			opts = question.getOptions();
			for (int j = 0, size = opts.size(); j < size; j++) {
				opt = opts.get(j);
				opt.setOrderIndex(numStr.substring(i + j, i + j + 2));
			}
			Collections.sort(question.getOptions(), new Comparator<Option>() {

				@Override
				public int compare(Option o1, Option o2) {
					if (o1.getOrderIndex() != null && o1.getOrderIndex().equals(o2.getOrderIndex())) {
						return o1.getPid().compareTo(o2.getPid());
					}
					return o1.getOrderIndex().compareTo(o2.getOrderIndex());
				}

			});
		}

		Collections.sort(questions, new Comparator<Question>() {

			@Override
			public int compare(Question o1, Question o2) {
				if (o1.getOrderIndex() != null && o1.getOrderIndex().equals(o2.getOrderIndex())) {
					return o1.getPid().compareTo(o2.getPid());
				}
				return o1.getOrderIndex().compareTo(o2.getOrderIndex());
			}

		});

		for (Question ques : questions) {
			System.out.println(ques.getPid() + " <> " + ques.getOrderIndex());
			for (Option option : ques.getOptions()) {
				System.out.println(option.getPid() + "<>" + option.getOrderIndex());
			}
			System.out.println("++++++++++++++++++++++++");
		}

	}

	private static String getNumMd5(String md5) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < md5.length(); i++) {
			sb.append(Integer.valueOf(md5.charAt(i)));
		}
		return sb.toString();
	}

	private static void random2(List<Question> questions, String userExamId, int repeatTimes) {
		// 生成md5
		for (Question question : questions) {
			question.setOrderIndex(md5((userExamId + repeatTimes + question.getPid())));
			for (Option opt : question.getOptions()) {
				opt.setOrderIndex(md5((userExamId + repeatTimes + question.getPid() + opt.getPid())));
			}
			Collections.sort(question.getOptions(), new Comparator<Option>() {

				@Override
				public int compare(Option o1, Option o2) {
					if (o1.getOrderIndex() != null && o1.getOrderIndex().equals(o2.getOrderIndex())) {
						return o1.getPid().compareTo(o2.getPid());
					}
					return o1.getOrderIndex().compareTo(o2.getOrderIndex());
				}

			});
		}

		Collections.sort(questions, new Comparator<Question>() {

			@Override
			public int compare(Question o1, Question o2) {
				if (o1.getOrderIndex() != null && o1.getOrderIndex().equals(o2.getOrderIndex())) {
					return o1.getPid().compareTo(o2.getPid());
				}
				return o1.getOrderIndex().compareTo(o2.getOrderIndex());
			}

		});

		for (Question question : questions) {
			System.out.println(question.getPid() + " <> " + question.getOrderIndex());
			for (Option opt : question.getOptions()) {
				System.out.println(opt.getPid() + "<>" + opt.getOrderIndex());
			}
			System.out.println("++++++++++++++++++++++++");
		}
	}

	private static void random(List<Question> questions, String userExamId, int repeatTimes) {
		String md5 = md5(userExamId + repeatTimes);
		int md5Num = getMd5Num(md5);

		// 生成base64
		int questionSize = questions.size();
		int optSize = 0;
		for (Question question : questions) {
			question.setOrderIndex(Integer.toString((md5Num + getMd5Num(question.getPid())) % questionSize));

			optSize = question.getOptions().size();
			for (Option opt : question.getOptions()) {
				opt.setOrderIndex(Integer.toString((md5Num + getMd5Num(opt.getPid())) % optSize));
			}
			Collections.sort(question.getOptions(), new Comparator<Option>() {

				@Override
				public int compare(Option o1, Option o2) {
					if (o1.getOrderIndex() != null && o1.getOrderIndex().equals(o2.getOrderIndex())) {
						return o1.getPid().compareTo(o2.getPid());
					}
					return o1.getOrderIndex().compareTo(o2.getOrderIndex());
				}

			});
		}

		Collections.sort(questions, new Comparator<Question>() {

			@Override
			public int compare(Question o1, Question o2) {
				if (o1.getOrderIndex() != null && o1.getOrderIndex().equals(o2.getOrderIndex())) {
					return o1.getPid().compareTo(o2.getPid());
				}
				return o1.getOrderIndex().compareTo(o2.getOrderIndex());
			}

		});

		for (Question question : questions) {
			System.out.println(question.getPid() + " <<>> " + question.getOrderIndex());
			for (Option opt : question.getOptions()) {
				System.out.println(opt.getPid() + "<>" + opt.getOrderIndex());
			}
			System.out.println("++++++++++++++++++++++++");
		}
	}

	private static int getMd5Num(String questionId) {
		int sum = 0;
		for (int i = 0; i < questionId.length(); i++) {
			sum += questionId.charAt(i);
		}
		return sum;
	}

	private List<Question> builderQuestion() {
		String[] questionIds = new String[] { "dc027edc-486c-48be-ad47-8281b83460f2",
				"1e8942cf-d91e-4d72-be3a-0d65bc77224b", "627b245a-6a7f-4a11-a3f6-bbdf4aca5e2a",
				"939be6e1-502a-4f36-809b-9514d1dbe0f6", "0ebd3045-5ed1-4153-b666-09a6237881b3",
				"98697348-8dd4-42f8-bd88-36c370e51950", "a69063c3-3ca2-4e3e-ac54-2d6e1a6317b0",
				"9c6a6c73-bcb6-4df8-8aff-bd541d01fbef", "668211d3-3927-4d0e-8175-4fe7be12450d",
				"23de1068-030c-4a2a-9d54-9b279439d3da", "42449e3f-ce99-4003-ae38-80ee72801565",
				"32ee1ddd-7c63-4e53-b898-873ec5f4ccf9", "f31c5f77-e74d-48b7-8a2a-ad4d7ff26bb7",
				"1c020d91-96f8-411c-9c6b-8c88a0034bec", "b2bcd449-1ce7-42c8-82e0-20e3e1260215",
				"d99e2a9b-970f-4999-b30a-1e00d3df0793", "9f858cb6-7e6d-4bc1-bde3-d0cb69557798",
				"c8e691fd-b39d-4eeb-949f-c355f113621f", "d4748628-e698-4f04-b4b5-de378ae09be0",
				"69b32bb8-bf45-4f43-93ac-bf40fc671030", "927d1709-5321-4cda-8312-dace8a89d380",
				"c6c7e174-bb23-45d7-b0cf-528f19aefa07", "3eca56ba-38c3-4d84-a18e-4b3a8112bfed",
				"442aaa0d-845a-4b62-a534-26a35d1d7ccf", "77d87919-6938-45a5-a95b-c437452160e4",
				"1fc5dee3-111f-4417-a87a-eac6e52ddddf", "9beb1647-07ba-4b89-a9e8-61dbd0aed1a6",
				"3d311a45-42d9-4923-a194-c5931a442612", "de2be9f7-d8d9-45c8-8153-6ff72a12be5b",
				"2a90d5ab-5e5c-413b-9f9f-f8a058aad9a2", "ec48dd28-9a4e-43e6-9faf-178db339913c",
				"36e8c81f-d004-47d1-92f4-baa93868917e", "2f105adf-3a97-4af3-9c5e-d715f293ad97",
				"58f1c3a6-c9ca-49a5-b0d9-85eaf675ed1b", "8d52ecc6-da23-4830-8eee-5022cce3c50a",
				"fbd7537f-d4cb-48be-ac22-b7f2d433dd1c", "ed155c9e-91d3-4de2-8de5-2f81722e640f",
				"80a8085d-692f-45fe-bfea-a673fb612190", "df37fa6f-1245-4c03-9ef4-9161549d6c27",
				"46157faa-ffb2-49cc-8bb3-1eb8f522819c", "3d2e8274-db69-4c80-be1c-91101ad00ea9",
				"fc358782-25e8-4c3f-a2e9-530834976dfb", "63ac9d44-22a8-40e3-98e0-01e189c803ad",
				"fae2800f-ef6a-4243-8438-af933717763e", "5cf96548-338f-4e37-b91a-cef5ec72289d",
				"38257cc6-8c4a-45d1-83e2-cbae8986cf1f", "a9431da1-d32d-4ff4-9e78-b0ec810c78d0",
				"a00f2f92-a254-48b2-9f87-e85a8f48501f", "a07e1337-737a-4023-98ee-4560ef241717",
				"c448a766-a8a0-4d64-8bcc-0314c68d996d", "c74cace4-a8f8-4105-9511-b2b93732b30d",
				"6a9f2994-842f-4abf-90f1-dda5283b613e", "8ed2a0e9-34d5-43e6-8e96-f21870b947da",
				"eb9be238-7468-4e94-a75f-3ae165d5469a", "864201d0-528e-4521-9ad9-55c9d73c9338",
				"e38c0b87-22b7-4ffd-b7ab-5a4f9ffed8ae", "bd1f9801-037e-4ecd-ab78-d73e6e6fbb39",
				"b957f030-f035-42a4-84c2-761330ef12ca", "41926971-a9ea-4c25-8e0a-dd7d3925c268",
				"4a466ef2-61c0-432b-a82e-87e1bc910f9e", "69eff860-049c-475b-8ad7-13aeccf56fce",
				"63e18aa0-ecde-44ca-9287-9f028094c30f", "841c9acf-d935-4e0c-b90b-748722abfd61",
				"920148cb-3ba6-4d55-ba44-d34767339982", "30b5ad8d-c1a9-4305-9ae0-de419a054aac",
				"6d2b396e-952e-46eb-b613-2e0bcb72d33e", "010ff11f-3228-451e-9fca-989d6cc4be0a",
				"5ba6a8fc-2156-45e3-b550-6c9237243561", "ee90dcfe-be9b-425f-99d3-66995eaab898",
				"926c51d1-f0df-4d70-b90c-c78eac95bd66", "ae03bf77-bede-4473-b8b1-01cedaad97e3",
				"3f718067-b054-45d2-a2e0-92212bbbb28e", "560e640f-5872-4dd1-9a8d-b2912aa07331",
				"98c850b6-d99a-496d-8f03-f308906fce57", "891a2f68-575a-40e2-95d6-32ae71aa3986",
				"ec38042a-0e16-4b40-a921-861d13169ed9", "ab0c2e34-5956-4c38-9d12-caa6c3d14cf6",
				"18c32f96-5dd9-4d61-8a7f-6dafaea22c21", "a0c77859-fb6e-4c45-91b6-6a51038fb03c",
				"69405974-9247-48d6-bfe8-2083cbec91ed", "18fd90d8-4618-4477-a58b-482509df72c3",
				"e2b669c4-9355-4def-bacd-3a241b0158e6", "582e3f4d-8a42-4aef-aeb9-ac47f26401cc",
				"4fc6f3a1-d109-496a-b8dd-6242a88295dd", "ddeb3cf2-68bb-4f87-862f-1342cd348a94",
				"79970f96-b142-4936-8373-8ddd6d72b44c", "ce51d7a7-ccfb-4a58-a943-d92c7fe54eb6",
				"1f78de0f-b403-4dc4-a4f1-077d67abafbb", "2da495a7-5e09-432c-bf0d-fdde6cb8c8d7",
				"ed2429a9-d44e-43b4-87e7-2af4246a122d", "5cc45ef6-34af-420f-995b-1c3ec776cc4b",
				"4ccad704-afca-4459-8c0e-134ee5b6ea73", "1298d3f3-181f-4396-a753-eef0ec64f8f3",
				"7c1b3fc0-0858-4c51-aad4-f1b5b4a52764", "cc0f74fc-b983-47f9-bfb2-3f8ccaceb9ed",
				"106ee003-bcd5-45eb-93dc-0d0382ce2482", "ac9ae168-9d0e-4034-ad12-78b3830a0563",
				"a62b5589-d8c7-4130-9427-5b3806bef12a", "b5b7cb00-500c-4928-9be8-78180a64b5ce",
				"e7688aca-2c05-4181-b714-7c5b13f81537" };
		String[] optIds = { "29b70558-9d24-4b2d-af1b-ae1752a37f7c", "74aa6b89-2184-41d8-805a-5ff1214f1b1c",
				"78ac1f5d-0a8d-4581-83d1-62b037a6895a", "108a3e47-3fd4-46b0-9fd4-e835f72f7792",
				"3e4c4180-f2df-4535-92bc-99dc71260169", "a1c692c5-6d92-4b20-b0f5-543785a34167",
				"118ded91-02f9-4d61-b2f9-8074c382ec8b", "691e99ba-0232-4899-90ec-9a95ce82ed79",
				"c03fe376-347e-4fa4-a6be-8c372db7073f", "810d8559-37dc-4a6d-a256-8166fee2a9ba",
				"92e7a297-eee2-4ce7-8fc7-63b3cfd4b079", "7bcfaaaa-5587-43b5-bab7-e51caea98aeb",
				"c78a4f43-ec22-4a4c-89b8-87344e46a766", "a201dfa2-fe0c-4da3-bd7d-6a0df8c1e580",
				"2f73fa5e-e5af-4533-bc3d-2e177fffcad3", "6fc6c008-ce08-434a-80a4-ae219c5c3d28",
				"bf454d5c-b14d-4925-8cc0-e0b23a3c3462", "5cd83219-1c2c-43a4-93d6-a31678ed3a45",
				"f4c11376-d020-415d-a719-a2a03630fd59", "5026b4d0-9e26-4b12-93ac-bd1434e0b3c7",
				"c8225895-7338-4dbd-8855-af49c2800c20", "3d107cd0-9dec-4e56-bf52-3f350b12c3e0",
				"8f1cbc28-7b8e-481e-8e77-1a992cead6e2", "91f98468-e0cc-47fc-9eb9-f000e2be8afd",
				"4fa4f3a9-9125-4bbf-92f2-e6c762fbddb1", "07bcd5d3-8287-4619-a69a-c8d69b882805",
				"86348587-cb49-4712-9330-eadcfd70738f", "da3a313e-0354-4f88-b4e8-24e7d4635978",
				"0152af27-9a88-4679-9f28-f95efd741f64", "8c7468d2-6870-4547-af0c-4da671e4a3d8",
				"b8fe9872-d129-4dcb-b5d4-2d458afa9e3e", "df9c8099-7e6e-47ac-86ea-eff31a156766",
				"7c8f434b-cb5f-4079-bc86-1a71122f2216", "7ced3353-703e-4e3e-b132-1841c45e1053",
				"54e1e200-1f4e-4e58-b998-bb6338622410", "dfcaad32-138c-4567-b278-45be32f345e9",
				"92315846-c4bf-47fb-b7df-833adc37ef13", "716221c2-0503-4586-b873-2efc67be50aa",
				"fd835f14-f125-466a-8b67-58e5d4c9d69c", "1a0cf3c8-3625-4b4d-bdcd-4d5bdac29677",
				"4ed07cc1-6374-4388-b262-fee8d4e9126c", "50a803f8-10f9-4cb7-b269-2a1b072df575",
				"90cc19c2-858a-43cf-a4e7-2af55c965b75", "68d614e3-a23f-4814-b7d3-97166fd80357",
				"6987dcfc-6e11-4326-9801-e8791c9baa5c", "dc58d39a-bb5d-4b11-ac8a-4ad85961b142",
				"7f4677ad-e736-42f5-864a-7aaeb787305f", "4b5dca8d-2166-43eb-a8d7-06457f1f7235",
				"9e1fa6e8-8170-4ed1-99ed-613043e70d62", "f46b662d-d85f-4941-8576-d9cc93c6dfdc",
				"7602b627-126a-4c70-8369-1f448d242d5c", "5e137cd8-584c-493e-8b77-bb73538ace44",
				"fd76edeb-5b68-49fc-babf-8c7c9685204e", "1262e22a-4fbd-4ca5-9721-66ed5a735e5c",
				"d3495247-e143-44a5-9338-8cbbf7418050", "dff04199-6ea4-42c3-83f6-bf9e591d0c43",
				"c8563652-5bbc-4bf6-ae22-e85591ef9e31", "a18abdba-e6e1-42c5-9564-42a6b451aeb0",
				"6b9f32b0-8393-41d8-be63-4f7eaf412b3b", "e9d630c4-3bfd-48d9-a275-ed87703973c9",
				"efd028a0-2e9e-4062-b914-f511cf515269", "9a70f2aa-354f-4eb3-bc4d-811011df9f5e",
				"12e4eabe-c5aa-4373-80f3-ab7def45f308", "890d0883-8949-4f6c-b446-fedfb920181d",
				"1c48dd5f-6d1b-464c-beab-e58dbf2cd6b5", "d60507fb-0a54-4fd5-b06d-27580431614d",
				"7acd5ce0-6df2-4769-b66b-1ff2a26f6b15", "f911bf12-c921-4a3b-9639-15b8754b9388",
				"62cb597b-b652-4801-bad5-1b23f6f18ccd", "9942691b-0289-446c-ba5d-4eb5034921c0",
				"d1010511-7615-42ef-b2b6-79f8ee68dc79", "7d09401d-091f-486d-9cf5-980940459602",
				"8d05b513-d5ac-4638-bb8e-5e3d5b386ef9", "47061c1d-d896-4c47-b84b-9416a709a6f1",
				"3a50b738-f493-4fb7-a95d-f4342077df33", "bba6dc66-3525-4775-bfdc-e4d3fc7f2211",
				"0231efe2-b07d-4ee6-bb49-3c2db0b54127", "200dffa9-78e7-4d52-a2ab-77619f6fbe20",
				"fcbab53b-723e-454a-b657-035f9e0740b9", "e4c0ce83-9130-4c12-8303-88c239e2de2c",
				"ac61412c-a25d-435a-875a-01f5f8e7e64d", "e0439b37-8981-4a39-a368-0754c857a88e",
				"e375ffb9-6418-48d3-8d36-cb6f4bb753e0", "47d8e3a0-6655-4273-8f68-12375ce5947e",
				"9b99b149-f9c9-47f7-b64c-e5e755067351", "b5fe7ab4-51d6-4207-92ba-c7b3b8d34f3b",
				"caa13030-6cfe-4441-b2a4-93ce8a48af1f", "d0ab147b-29bd-4db8-9ea8-345e1a0b125f",
				"ea2ecb9d-8353-415a-82a9-d065f500f9fd", "3e993c66-265b-430b-82d4-75e6d941b23e",
				"17c011c6-30e1-4b91-94b6-3e1cdeca826c", "acbc9b42-967c-4d27-b90c-4ee43e46d3c3",
				"2b38f95f-62c3-4d51-865c-228df8fd9eff", "47616286-5042-488e-901e-6e3b1c90e1a0",
				"a5d12edf-d00c-496b-946d-b2d7c2d7adf9", "fd752c7f-c70b-4a00-a153-a1775fa540cc",
				"8670351a-199a-4d6e-82fd-c90b1a6f56d7", "1c4164de-4547-4ce8-9206-6bd166706407",
				"b09c27a2-916f-4dba-9fbf-b5489efe7aa4", "c361bafc-9fa6-4971-b917-6b58eda8ac56",
				"ac947a44-5143-4a95-9f50-488390c8c28e", "c8145be5-8e07-4d6a-ac0f-1f5a22c252c8",
				"6b5bced5-c0cf-4a58-a297-3366386358bc", "93f3c9ed-510a-4529-9958-6b5f5c2abccb",
				"484937f4-f864-4bae-99ad-d420a4a8e833", "324d4975-ae11-4d6a-9651-ff4275687cee",
				"38ff2918-354c-4d96-ad35-e4788867c254", "123a0f92-9afa-4861-8de6-ac03f886688b",
				"b5ebed5e-9c25-4b1a-9283-06bf276a1522", "a378b0c3-24ab-4b0c-a529-f1d25a153885",
				"ffad44ee-461f-4102-8938-7077cdce3041", "cbb9c39c-be9e-43db-917d-8f5316135142",
				"165a6223-cd9e-4638-a298-92cd0e8ec3d4", "91e13ba3-2a4d-4ee7-9b73-6e36c9fa7d43",
				"d309d37d-6361-4787-ae66-e3b55ace0523", "9b2dce65-e32b-42ac-b217-716b5be8e27d",
				"f8bcc057-3d6d-40c2-8aaf-6ab427f9cbe8", "65689327-a29e-40b6-ad16-485bbfe122fe",
				"36ac72be-1725-4a00-b4a9-beb87bdc7457", "47b1e3d2-6705-4955-884a-79a9cdca1552",
				"5c4a2cf1-2ccf-4067-b116-bcdb5a6c8c59", "a8f36c95-d512-429e-aba4-db9399c1cc10",
				"21d4e93d-0435-4f36-8597-0956ded593b6", "70029676-d35c-4d92-8395-bb4cd46f21b5",
				"e049e795-bbfc-4d8a-a2db-f7a069d42bc3", "73dcd1f9-7958-478e-98d8-203d8c7cdf21",
				"a7326808-33b0-4207-ac4c-f18c487010e1", "a4c28f6a-0acf-4d96-a85a-ae33d8ad8a4c",
				"82eff7b3-8e2e-4be0-8da6-2cf477416d3b", "738514d1-c9ec-4c84-8d68-c436c53f3a7d",
				"f25cebd2-b284-480c-801f-e3f44a379a38", "6b8a7941-d9da-4e93-a753-db461aae9975",
				"d0dd5ee4-ef62-4d30-b8d9-c38bcbae900c", "a55f51bf-3758-4a31-bd2a-ca96bfada74d",
				"8d1a629d-45e6-4eab-ad29-88ed31d34820", "5b83c4b9-9ec1-43d6-8370-d675e0046d89",
				"06c537f6-2653-4633-b13f-af92bda4eca7", "b87e1e15-0c9b-49d9-a443-4b8747d8b717",
				"cb2a0eaa-c1c9-4598-a7f3-e2ee07ea790d", "21e43405-d165-4325-8ba3-23abc9a1a886",
				"b329a211-0c24-43bc-8e7c-0aa58b3fa722", "f656e286-d0cf-4b90-9dfb-f8b44b38ee7f",
				"839d5c87-f94e-4da9-8e73-529d0af3291e", "0ee2cfc0-dbc8-4409-8d05-d1ed4acd2603",
				"9f881730-74e6-4a13-8005-6cea6d987f70", "d3eaddf6-3175-4ed2-9f32-2cfdad336453",
				"e10b5c50-0eed-411f-bfa1-3432f3c80499", "d964d8ab-d3dc-4866-b14c-d0c2f72b013d",
				"c3b676ed-cbd4-4337-ba6d-4a0ab4976770", "b611e505-3e04-42c9-b6d5-7093bbc69ff1",
				"fc0e142e-36d5-4cac-acb8-8e24c30d0ff2", "f32d3f1c-f1d7-42a9-847e-c731268110bf",
				"3d63e92d-e428-4d35-bc63-c50063431714", "018553ac-5714-47dc-9c79-fc7b0c7f659b",
				"afea2a9f-9477-4295-a1f8-4f8ee800ac5b", "d5b78dfb-1ce0-4849-9733-e0835275d867",
				"4ea01bb6-770d-4ae3-9970-25de53187f08", "ea5ea606-f195-4290-b1cc-2bcc05b5dad3",
				"e03a7e28-b5cd-40a5-ad43-06e6466d677b", "99c8cd7b-9763-443e-88c8-505599b6f125",
				"65249c97-3eb5-4ee4-b954-fc594f570945", "55a3c917-29e7-449b-b27b-4d4324382079",
				"0557d33d-903c-4336-b5f6-5830bab7efa3", "fee0538e-3a82-4c56-a758-94a282af5402",
				"3e2057c9-b27e-48b6-8dfc-6407924efcca", "2412e6b3-e323-40b6-87d5-c05d37ac061a",
				"8780bfda-a9dc-4a63-8484-eb2bd99c3444", "027744d9-8cce-4b16-bb01-a4d859a64140",
				"92e73869-ad58-4444-b073-0724b429f314", "b986f1e5-19a0-4c8a-9639-318e448a2053",
				"c372cd0e-fcae-480a-87dc-a4580ac84430", "6c168912-7f2e-4b48-b4ac-a93173eef7b9",
				"92cf1660-c0e3-4b3c-9558-1379de7544db", "3acef491-e227-4381-895a-f3120f364def",
				"745cd035-75f4-4c26-91e6-d032d6d45f51", "bf821441-23e1-4cd2-894a-b6c2ad673b7b",
				"804cce8f-fe83-46f3-97ed-e9544c8c57ad", "5cf93de3-0dc5-4ff0-8be4-9aee13b4a051",
				"4b81f8a2-557d-42fb-8298-ab532dbdea6d", "0a7cc670-8c6d-4a18-bb47-41479da06e30",
				"0e646c43-489b-44c2-a889-fc02a71f3710", "e04b38ed-c6e3-453b-8122-87ecd2d483f8",
				"ecd7ff13-dd16-443d-ae63-e5530d5047f4", "5f379f0e-2369-43fd-86c0-71d40d4c98b0",
				"52002804-0b20-408d-97b9-502cde683a80", "7ab8af17-d89b-41c4-a562-361570698a3e",
				"93893434-fa59-459e-8b2a-6c6c420543cb", "3378866f-c932-4b07-ae01-dc632f45859f",
				"c39cb208-03fe-4470-ba50-7fc058c35704", "0c101c07-a111-430c-a0fd-0bac27a22d2a",
				"a7232bae-eb49-4085-974f-d3392e0b65fe", "1203917f-3a00-4678-8b7f-9baea09f6195",
				"9fb0e93e-b548-459b-86d8-f9a179f19d31", "0f1c97a9-7b34-4f2b-9236-310c2af436a1",
				"10b187a2-8789-4090-a31e-c8993e3fbb15", "9b6a07e2-5180-4ca4-8251-8d3fe22fab77",
				"6c441d19-edda-4e01-8475-cd2eacde98f3", "ae80ae88-fff8-4ed7-844a-f27e292bd0ec",
				"b52dfa7c-79c6-429e-8893-e859ba095247", "5bdba4b6-3f4a-43b0-ae44-5d700c324d28",
				"c2783a9b-59b8-4580-87ee-84b887f1dd97", "b11b58d2-e659-4778-a7c8-326db96b5f96",
				"17cf740e-dfb4-40f9-b0a2-dacc2b82dfaa", "04b71a58-b9c3-43a6-870f-39492576c2f5",
				"51e5d2fb-c4f9-41ba-b6c5-c2f8630c26e0", "3e420f97-95c5-4d8d-ba78-c4d906e0584b",
				"d48024de-93f1-4cc5-a572-96769dff0df9", "8a86c831-06d5-4a87-a359-a330a15537c0",
				"febccbc0-1957-4304-8f76-809f21f26aee", "462ab26c-9101-4d14-a7c0-90818be0dff1",
				"958214f1-14c8-46e6-a4f0-1411e7d9c2fa", "a5d067ad-cb73-4891-83de-d1cac485b30d",
				"501074a0-fed8-4243-81b5-17c59a67239c", "f26b093e-b674-4cb7-8db7-7dac17c2fcc6",
				"2e3d2581-d19b-4567-b0f2-8344f3ee39e8", "423cd432-8a6e-450a-af57-d1f83a72eb70",
				"cfcfc736-dcc9-40af-b2f4-0f62bf9a005c", "bc044cfb-0e64-4782-801e-4666fde9a540",
				"7aff86c1-d1f1-45d6-90ab-d6d14a7ec43c", "d918e34c-ba6c-40c2-b037-ddca85a27846",
				"14dd91c4-4853-4045-a72a-c8d67b097088", "15772d15-729f-4c1a-a7fa-76108c5feff1",
				"30760f74-a56f-49eb-9523-930fa7f181ff", "58024a8e-ed6c-40a8-82e5-a5ac70e6b88d",
				"1fd3df3f-0894-4418-a330-445e161d6c57", "b3e1a779-5e75-4892-83d4-d902571a087b",
				"4df8f555-9abb-4fe9-950c-4b3ab77d5849", "e21a95c2-f0fe-4bc5-9af9-dc15edafa7c5",
				"446aeebb-ffe2-4141-912d-9b696e30cb76", "46737ca3-2c1f-48ff-8b97-b486890603dd",
				"6a6e7d3c-13c9-4578-b8b7-b96badbe13e3", "d08bc036-12ac-4364-b81a-cedaa7ae3fff",
				"7a30adef-1855-4a7f-9d33-d963fb0c14eb", "30e23e83-a8d5-4cfb-801e-c3af81bd39f1",
				"70bc336c-9d59-4c9c-bde8-3adf756b8743", "48ac1091-e3a9-4c9a-9d8e-4199ea40777a",
				"730a78b1-ff39-4f2e-b7c0-48e6cf737ca0", "ee91981c-6a47-431b-9ca6-9aabe92988cf",
				"81dd0e59-afce-4f12-9a9c-9dcff6ef038a", "6f487aad-1e52-4001-ac63-8af253b67172",
				"70fd4a33-20da-4ad5-a8a9-97351a3678ce", "4c05510b-7054-43ea-88c7-888913dfdbd2",
				"d6334008-7305-4ebe-ab8f-267d0b5e6373", "b5a7843a-48ba-4b7d-97cd-5451cba5f8f3",
				"d9364780-c007-4b5f-92f5-2bf0a660134f", "c1e2344d-0a77-4d9b-8885-5ae7ca0e1b1e",
				"c8224224-b60e-48dc-b89d-01da272ceef2", "ce85c581-6c06-4691-adf4-907554f83dd5",
				"92b86768-d720-462e-9f76-fd0f1ebf598c", "32e8176e-77a3-4918-aa0b-a433e9ab79c7",
				"72793b0e-97e2-48b7-96c6-cf6f647b6426", "ad1a53a1-c052-4146-ba33-9ea87741e801",
				"2035fb7a-c66d-461f-8853-080984ff51ef", "0c744416-d7cd-4f02-ad2c-657f3fc93a24",
				"bce3afb1-e9b0-4520-9ea6-505258fb1ecf", "c93ab65b-c578-4f0a-95a7-0687137c9f11",
				"22a5f210-b101-4665-afdd-e5c82055a315", "d4decfe6-ea70-438a-b6d5-b83ccb3b80f7",
				"50aee5fd-3ebd-485a-bc5c-4840ee6c52f2", "49dd70f9-f0d0-49c9-8f64-997679a08e82",
				"42714fba-24f5-4f29-8a57-403a7455d4b3", "72069e3a-1c9d-4d02-aaf5-af4c3b772fa4",
				"59c7dbd7-4514-4d54-a8f6-359dc969b0ad", "08a4df6b-f5c4-4c7d-90a2-173bcb4179a2",
				"d3886e5a-36ff-4e56-b6d0-f133f1606d0d", "8d5be9ae-b600-450c-a507-adcda8c16ec2",
				"d11b543f-9aeb-41dc-bf99-3a4b90f439c6", "134d0d52-57bf-4ecd-88a9-0c274d397af1",
				"769148d4-59ab-4716-8011-4240cf20dbea", "9e847027-223a-4511-85c7-6e1a48df249a",
				"bac9dad0-ca3f-4721-bc04-ca03391a84a3", "e5d5339b-1fa4-457e-977b-c46f5be78453",
				"c0eb70ec-28c3-40a5-937e-171f6fde351f", "ec363340-4df7-4f45-85fc-16b022b54d29",
				"212fbe47-67af-45c2-8fad-43928e07b214", "c02fa15c-80a3-43de-ac78-8a3aaf1259f7",
				"a5528502-cb89-4afb-81dd-63023c6eb27b", "847c2a1d-d6b3-4488-9105-6b7ff4ac4d27",
				"c691bbad-d43c-42ba-9ace-7e5da8d4c29d", "1a2c4636-a799-4142-9175-f9f300ca4097",
				"d8e1e262-5994-424a-9181-bc67b5c7d018", "bf869603-72f6-4a9e-8b6b-90ba37c3edd4",
				"cb0466c3-fe8d-4eb2-869a-95def712c27d", "748b71ce-e372-4c5d-a6da-cb6408409a93",
				"c3a85481-4b4f-4fe0-8259-e376c6c09031", "35778beb-c732-46ae-b0bc-838dd7fda891",
				"0877d296-0b45-480d-899f-e04cc2a4e5b1", "56569dd3-35af-4483-b7bb-b808dd5c46df",
				"5955901c-4635-4809-a6f9-e148eeed310d", "057c2c6e-8244-4a47-bb74-445863534d03",
				"5bd8c89c-320b-4cdf-9f92-0be51c6c1b7d", "ee5b6576-d403-4dd6-b1ed-9efe82fd7d8b",
				"fd97d064-acb3-4fe2-8688-7917729c5a53", "8411955b-9342-4280-9910-69d9a626ba09",
				"0fc2e913-a418-4092-9901-0d664036903f", "d8a027b8-4241-45c2-9f82-fba4e30fc4f8",
				"231716c1-4c94-41ef-a515-5c9ecea08941", "3380a1aa-4d70-4005-84f8-0511fa719494",
				"90d09dea-9f8a-4944-9096-08bb6b356834", "93210c5b-6a1c-49cc-a422-f1ef74e4e43a",
				"0e7b746a-6f90-4d24-ae57-df2f16c243d6", "5055adf6-ec2a-47e3-990e-4960390ef269",
				"6b543636-3926-42c8-b00a-9a21d3584ca2", "d0e02fb6-0611-4524-82a7-bb7e8cc536a8",
				"94ed357f-fa92-4231-976f-ae793873c17b", "07a7cf42-0a57-401c-b433-c8358f6025fb",
				"7e297a3d-ed6a-4853-9199-b7c94ec12385", "8fa5a86c-4686-450c-990d-5f22a5882923",
				"a39b76a6-5aab-404e-823e-b355cbd6bd5c", "745c10b3-1b8b-40cf-92fc-07374c5e7cbf",
				"b974abb7-0286-4699-b1ce-33be3e180e09", "e3c0ddeb-4649-4a32-b890-3e8ccef5e8f0",
				"3cdae941-3e15-49a1-9edb-48f88dc2a0c7", "d72c8484-4dce-4452-9bbe-b15c4e799b31",
				"55f76b7c-99a7-46b1-a9d0-44b58d498904", "d1cb13f2-e355-4f17-a123-7dcbca39b751",
				"ad56a08e-8651-44fd-a8fb-0e6b68eed953", "4d47ba42-f41d-443f-b536-14fdc069a010",
				"f10648d1-9a53-4c1b-8c54-3b02644c7bc5", "9cc79893-b1e1-4616-a3e1-7873114038b9",
				"0b95c2fe-6800-4a7c-af34-dd02dead67de", "182f8de0-1bb8-4fc4-8ca9-c3a4c334cf9e",
				"03f7b459-7efc-4677-9652-818b77647c25", "10dae2d6-7cc8-4528-ba37-5c508dc6875f",
				"54e88bce-a250-42ed-8195-eac3554e1dc3", "c4d4ab76-c4f4-4b5e-a6c7-d390c8315765",
				"455a9890-f376-4699-92b6-16998b4e1381", "85505033-304c-4827-b434-02b62dd8c6cc",
				"a69904be-6561-4c6e-8eeb-2729554fe0fa", "e5ad4fd9-b10c-4096-b6bd-83d03afeb43d",
				"a657bf17-51f2-4b9c-8ba1-1554843d7118", "faf0e800-dd62-4919-afd7-8460c4282153",
				"f0cbef80-e720-491d-ae9c-699a9a0e7866", "52ba5b16-4f6b-4119-90af-2c1128ffc956",
				"cd6a0db0-4f79-4659-8858-451268bceacb", "0913a144-0dc2-4c58-a923-4c9bd7684a02",
				"73fea1e2-6219-4363-a890-7fa7905965ca", "83e83d61-f7a6-4740-bac2-0c5086a6f8b1",
				"1a8d9ffc-8227-4ad1-aa43-d369e6f87465", "36895ada-69ca-4107-86bb-caa4e26165a9",
				"9ea901e7-07b3-4090-a902-04328252b409", "45153d06-d289-49a6-9746-b6b4571cd200",
				"cc673db5-5558-45c4-99a8-cb0c37bf3419", "6ec0cedc-38cb-41d7-b9e9-4f6346421d43",
				"e126008e-6e4e-4f74-85eb-17ed45f623b6", "09b11f9f-14c8-4644-97b4-beac5ea8b934",
				"98db94db-345b-4329-b07a-3a50fe0b76ab", "8a5ed112-72ae-4cd9-b03a-d6ff73650b2c",
				"54e47a9b-a9f2-43f4-aa5a-cd04e3bb6e27", "79fc7b69-a7c9-4a9b-9d08-80f40efe58b8",
				"d13d2153-a695-48d9-a256-8c04071cb3b8", "ca5e80af-d3e2-4dc1-85a1-df74deec76e5",
				"75164ba1-4330-4226-981b-49efc8b0af75", "4cefe672-f895-4143-9ece-c489d7b0a6ff",
				"352f67b5-2978-4491-9202-2e07df6816cd", "fb4c4fee-5ca2-4720-87ca-d1c5b47f02e4",
				"d42d1aa3-15ea-420b-b017-36e8289934d7", "0a657716-954e-43c8-a0e3-e027a770bafd",
				"75114598-7228-4632-853a-72fed5bab676", "b5c48c0c-30f6-4483-94d6-c3f557102dd5",
				"ef59ca80-cf34-4e6c-b8cc-ce4f9510e7f1", "c195545e-a26c-452e-a5d1-38bf7217e6cb",
				"a84783f9-e05d-4cdc-9bfb-e7ef8be292c6", "5293113d-fa1d-4928-a4c2-0609278cfc77",
				"a988d9a5-fd3c-4c43-8b41-658769dd6f78", "b02a06e1-8ff4-4198-9be3-fbee347c5d68",
				"ba767008-3a79-4603-b07d-34a7ab486351", "ce04d948-29c0-4204-8721-0c1cc557abbd",
				"02906241-c602-4ab2-bfa9-3883e4e2d1e1", "5f344cf4-3bfa-436a-ba51-6398fe61ddff",
				"cb791972-a358-468b-892d-2b62d39e292d", "2496ef39-955d-4291-8d9a-30a21f5e52eb",
				"4d15f351-4961-4234-8235-ce0eeeaf783a", "c683503a-a941-444f-bfa8-2e9d4e0a8eb8",
				"774e8354-1035-4904-b706-2629e6ae6093", "d65f0346-7990-4535-874d-19bb6120abb1",
				"a6ca4e43-ea64-49dd-8e9a-52ad20ca1614", "add1cc73-7a93-4d7a-a0e9-fbb44078f5aa",
				"3a49cf8a-67ad-4baf-8fa2-f2e7fedf6ad4", "8ee9494e-7377-477c-b509-f425ea2b640a",
				"a5d81bb9-9b02-49a9-9e18-d15d68ba295f", "902797da-6240-4eed-b1dc-d83ed3356d0e",
				"c05de695-a1f2-4d83-ba72-dd51c7e3e6d2", "9787ce37-4ee0-4afb-ba38-7d5b9c473abc",
				"8b8aeb5e-4c1c-4fb8-90a8-90315e535fc1", "6541e138-395c-49a7-ab57-52ac357d6826",
				"9e3d1a6f-f8e5-4588-a30b-f40da75bc191", "c4c3866d-f34a-4a7f-a485-1487863bb4be",
				"a2951be6-5862-4786-80ec-9455e47d1317", "05d42d6f-0a75-4cba-8c0b-e1ba0f366b8c",
				"642f5dcc-4a06-4d64-abc6-39c9be1bbe61", "0fd10826-bb76-4998-83ea-e4394a49979a",
				"fe7a5130-6729-4f50-b075-8ffe35549a03", "8cd032d8-ed3a-45c0-9a89-73281db0bcc8",
				"dfd41103-afb5-457b-aea7-daa1394b00e6", "d47377d4-017d-42c3-9ac3-1dd0ca309973",
				"4502810a-5966-4da1-ba30-04d446302585", "0b9b3c46-7f07-449f-a44a-9346fd8a9cee",
				"e3a40fb5-a2dc-4845-b34b-2baa8a16023a", "ef499a28-037b-4b5a-a785-7c00700519dd",
				"d9cff21a-7b74-4c52-a6f6-0c979454b5a6", "ff3f6e6e-2a69-4c73-84e1-3df280c911d7",
				"92c000ef-3a28-4744-a7cf-16b65bc58063", "294eaae6-0b03-4eb1-873c-f65c86ce5a8f",
				"de5d0c73-11b5-4313-b25f-f8315baebdbb", "357d7a9d-74f6-430a-b17f-7e495b35312b",
				"08447afd-4e7d-4dbd-83ce-189c7390a462", "7fc78ff1-8dbc-401d-a1e4-e08b17b1e13c",
				"fd33bc4e-baed-410d-9ecc-13138298edf0", "1ead816a-58d2-47a1-80be-c4bc0ccaa002",
				"274677a3-c3c4-4371-8544-e22e3f5faa79", "11928adf-ba48-444b-a898-d63802b78bfe",
				"33c399f8-e773-4bd8-8da8-c0918291de4c", "63e7df31-1367-4a77-81bf-edfe183e49e3",
				"c305b8cf-c887-45a3-95b3-4d26118db18e", "9f677930-8be6-4224-83af-facbfadea538",
				"036f5d24-c449-431a-bbf4-d77bdfe14b9a", "3ab9112e-cdaf-4dfa-ab1c-a6584cc06b1b",
				"ff824ac2-03fe-4624-861f-37efc8dfbe1f", "0289fd7f-0968-466a-81f0-91fd19bd1c5b",
				"5fafe97b-0c8f-4cc8-beb5-9a3d2b0502f7", "262ffe0c-3978-403f-bb6a-f00b7f8b0b66",
				"3c8578b6-ff42-499d-8937-658af844915f", "d330d6ff-48a3-4d9f-9df3-24eccad3cfb4",
				"c588b136-4b5b-4645-8184-84cf3171239e", "354db916-7a93-476b-aca8-87cf8242839b",
				"92e52665-5e8f-4f65-ba67-24c0f0a1c5fa", "68d376e2-eb36-4039-92ce-e7019570cf13",
				"f99affd5-7db1-467e-86c0-24decb7133b5", "3979f119-475d-4fd3-8b1b-bb5b27f6baf5",
				"889b96ee-a602-4365-99c8-4bf934346e02", "e0811897-71db-42fe-af64-222ec229d9b3",
				"d0c69e03-b370-4ddd-8a8b-f1099c57b305", "a7d8dd19-7fb7-497b-9758-db2454e6ed2e",
				"b990fb47-18d3-454c-a866-a3155e6acb08", "56997cd8-7939-4029-b510-593d87e9a353",
				"5b85c64e-6c04-4ee3-b972-1e2db40e2d6e", "fce187c9-f5f4-4245-b220-59f2223caee6",
				"4eaafb58-4723-49f0-b882-ccdabaddc786", "ade4a2a5-24e6-4691-9e4e-c663f953a385",
				"59663acc-87fe-44e4-9eba-23bf1624fe9d", "5018a328-d6d9-40c9-bd63-9d940abca900",
				"a147ad56-a70a-4c00-9b6a-999b35883e86", "27f7f015-7f41-4bc4-9d1e-be9e707a212e",
				"236fd2f4-2115-4e8f-84a7-c7c59aae2d71", "ff972ee5-951e-42b5-a963-5512e571446e",
				"50d760e3-c663-4117-9fb3-fd0ec4f5989f", "304775d9-809f-460e-a467-3a19a90b2f9b",
				"52b818e6-c80c-4e0f-9096-a2f28ca6dddb", "aca0de5d-b8a8-4879-8ece-67d4f49ed4d5",
				"6890d51c-28b2-4a2c-95a5-c9e9085fc10b", "570bb2b3-d49f-4b0c-b2f6-915f977b193f",
				"4c6dac01-3d2e-439f-9e0b-1a365ab2f441", "10558e96-8444-4d04-bfde-ea54c549a8f6",
				"691c0f0f-8098-4098-b5e7-598a234653f0", "146ab850-f4be-4f3e-ae3b-7f0d203c943b",
				"72820069-6a64-443e-9389-58865a6d9799", "6a056c9e-88e3-4270-add4-7f61e78ebc7d",
				"84eef0b0-a255-41c1-8b43-8c4936307a5a", "1429c11b-f893-4fbe-86fa-90b48e6e524f",
				"e4d50906-2580-47d7-8f03-65f20c2fee5a", "e9dd0075-f57d-49e9-9e5f-ab7740bd6105",
				"f522ff97-14da-4c3a-ab2f-46d0ed0d0b50", "e1ca65f0-d684-4c0c-979c-1bfdfe0f0952",
				"1e1b1d5c-e5cb-4e29-9d95-9eaf6850d0b3", "1bc9fe04-c6f5-4da9-9e14-f3cabf547f3d",
				"38aec85c-dd4d-4c0f-9875-b9360b6714fd", "8e7c2c4c-2b9c-462a-aa1b-16d96035971e",
				"8abf4a31-1722-44e9-be2f-ae5f70820923", "20e0855d-aef6-4332-a8cc-8868618f456c",
				"d0242f33-cd48-4537-b04a-655df0948801", "d9376dca-bda3-4654-a380-5dd9df2ab4f6",
				"65976be8-128a-45c3-9fe1-3853b59ffdd5", "3e3f5f3e-dd80-4abe-8249-fecd80ff3940",
				"59944dfc-c5b7-478d-be29-24a4ee6610b0", "63d0595d-3a01-48d5-9f00-9b1756b113fd",
				"380f5876-d485-43f3-8d1e-bdb43d4d8e7d", "98851347-5c2a-4146-b159-f5fd8718002d",
				"aa4dc6a2-4105-447d-9b09-6f6ea87c91b9", "607c0c71-887f-4ec8-8f62-1bfca15b4cac",
				"f7e2efd6-312f-4bd9-8b52-6591146ba9ff", "f6caacd3-8dd8-46cc-bea5-b73fb3be31db",
				"d0860587-ee07-490c-9670-044d70921fbd", "a53fde5c-db35-4f21-bb93-05ef5f9b95a7",
				"b224cb5f-a40f-456f-8672-cd79770bb16c", "de802c03-e087-42ac-baf5-11e176009978",
				"4986bee0-982e-4949-a307-d7f4b286dbd0", "d72b8590-0333-4c6c-8d7e-dd516f686658",
				"2acc52a4-2c2a-4fae-b326-9663270d1c8c", "b114587a-1dd8-49c8-98d7-f3a2de11d0c2",
				"680b53bf-ddb0-43b0-83e0-8be447e0d65e", "fab71821-c8f0-4818-a45d-14cdce575de0",
				"f5486077-6a3f-43f1-9108-5e1322629dad", "bd0cd819-5d0c-4f15-aa35-f12a6b603901",
				"39c3dfad-fc13-4470-b585-e9ea4fbf45c5", "365f4faf-75e4-45df-905a-0a91d16aba8d",
				"5a2111e9-e0c1-4797-b5a5-738f6eaee162", "98fcecd5-0709-40a2-8218-4f85ae2e94a6",
				"f3ccdf55-201c-46a7-95db-068b118fe3bc", "bf4c3b73-563b-4916-9f04-e05ee8785ffb",
				"ea93402a-8931-489d-a758-153a5098d231", "c8dc9c60-3127-4c93-a069-5ea6fe6851e4",
				"65e37d53-6471-4116-97b5-c3493129be89", "02312cf3-fdfa-4a4b-821f-8e6e3ce35746",
				"1c20ae5d-e173-4e74-87ea-61f478c2a3fc", "62ee4aa4-fead-4ec4-8c5c-d80c22e1eade",
				"5587a52b-bdfc-4974-a465-7f4a0bec204d", "73d99b96-d304-4fb6-a14d-c3c2dfd7876d",
				"d7e0e622-ece7-48ea-bd37-f84afcd09f02", "c8d5335b-0644-42b0-82e3-53819404b406",
				"31686ea7-1c85-4246-b8b3-ebc65f504be7", "3499b9d7-d84d-414a-89d4-7454b8fa30c1",
				"746adaf6-d4b3-4ea2-8fbf-c48d19d5a5b3", "82a57aa5-c04d-460f-ac75-23fc39426470",
				"5aa72c7e-2015-431b-9e68-595f3ed3bd63", "a2446583-6df7-4c66-8fa5-934b65a14edb",
				"f39e1d09-5819-4070-b2ff-09a525742fb7", "e7f760fc-6c5f-46dd-a003-b7f441be2399",
				"e1d0a0d1-9896-4ef2-8b1f-94ef66d7b1a5", "e5f8f353-5b84-4339-9cd8-3d8fe601129b",
				"01216b09-eb05-4d4a-9c70-67385c8d4edb", "91cae8f8-9d4a-4b79-9766-3c61bc99e76e",
				"b490eb84-43b3-4356-9e06-416d4a48564e", "a85b66d0-ab08-4d06-bd2a-d7a8efa80c3a",
				"2be88a67-2fdf-42de-b4ac-d2eadd674fc8", "14a51075-6111-4f45-ab2e-616cac7432a3",
				"05354db0-c73a-4a40-b83d-31aad460fc8a", "5f66fd89-bfb2-4d7b-812f-a811dab3c634",
				"d8908bb3-c831-48f6-a31a-805b09818017", "e991b450-ee69-4024-aeef-f9c6ed06f300",
				"f5a01497-4918-46b9-acf2-76380fe560c3", "0ae321a7-e5ac-4978-806a-81622d01ca4d",
				"75e2e8c4-7997-44c1-b4d6-532c5a43687e", "c5de392a-2854-4f70-a21b-9864db2c15aa",
				"0944ac4e-d456-4225-8eaa-7a00d9cf08ed", "4a4514e0-a7e0-4d80-b0a4-1d43a6303aa7",
				"d6a3257d-961f-46c3-969b-add0776ce657", "c9bb885d-1e73-4d0c-8050-662e224107a6",
				"414b12ba-b3fb-446b-867b-66b6b89b7ccd", "c3c1b9e1-956b-4b08-8d08-da47900b1342",
				"e77223f9-a44c-4e43-b09a-3fdad0490280", "abd94402-5811-4fa0-b69b-37660731908a",
				"98de66a0-57e4-4bdd-b8d8-fc8c4b6cc441", "8a47dc89-eec6-4eb4-ac44-9d75a4800ebb",
				"4811698a-c9e6-4c68-a715-25409c80a1b7", "3b5e4243-0030-421e-865b-3f255e344b0f",
				"973ee0ee-a47a-47a1-b086-ccafd3b5b13d", "4e8d4c85-83d2-404a-aaf7-38d30d64cc97",
				"8003fe17-9dd8-49b8-bb76-e9b388008ded", "11c63f3d-66de-45ec-8a82-b9ded4e5d547",
				"971ebdf0-278e-4c3c-8610-aa20151ea6ac", "eeda726b-875a-4f02-899e-34567cf17ea5",
				"8be8db23-5de9-4936-b4f3-ab6244a799dd", "371ee350-fc57-44eb-b41e-b965c6dfdf6b",
				"aa84bf35-54b2-4173-ade2-08fa57341858", "1849eb52-75b6-43d3-8dea-51d65e95f714",
				"45d1fb17-f4ee-4570-a25c-b46a237d096d", "f9c2b3d0-33ad-4583-8fad-219bff991601",
				"4d1ce1d2-ef9e-477b-a96f-69d5f1cc6cf6", "cc8c3869-fdb0-4375-8087-4a46ea46b6e6",
				"2b8d63f7-267f-40fc-8ef2-8e4a5f32b40c", "b2310010-fd6e-4dcc-97d7-dcc77a39a473",
				"23c9d07d-6db5-4407-9b8e-1a8dcfac643e", "e44aa626-5b7f-4cb2-a081-f8154a2e5eac",
				"b6cdc820-496d-4cfc-b368-d44373f192c9", "1492e6e4-876a-476a-b75f-5e7c712e122d",
				"0ea23ba1-1a25-4eac-b87a-77851ef5b2f9", "65727f63-d869-4d18-b04d-8b74be192d29",
				"49bce43b-6aac-40bb-a8e7-38616b3a70eb", "c7113902-05bc-402e-9866-e7d0da3ebd62",
				"ddabfcdd-4195-46a9-8b56-5c276b40bcae", "0fb3ad17-90dd-4687-8825-08d16adf0dfe",
				"f75dca2b-8618-451d-8c5f-05668d98057e", "37dee8b8-d3e7-4bd8-9a79-f2f93a445759",
				"82a17505-8e91-4391-8b3f-c4aca710862b", "a871c371-e90a-4cc4-b115-a2711fe2a550",
				"f37a51b9-9067-480e-b752-a6b34ca1fe3c", "eea17e46-7247-4601-b632-dbd5256e221d",
				"6f6930ea-65a1-46ef-bd5d-8f8a21a1195c", "2669713e-a205-4f3b-ab7c-61c0b03a0e48",
				"270edbfe-9a47-4635-810c-39de44c8403b", "d6ffa515-b2b7-4bd4-9a89-5bcbd799e7ab",
				"42aa11b6-ebeb-4da7-a395-c110e592de91", "db51d8a2-5479-4bb5-a63d-b08f14b99a27",
				"c6907a4c-668a-49f8-826b-bd1cb224b083", "4eec6fc0-9a4d-4b6c-8150-7559a20eb3c6",
				"f0e12883-6643-4284-912a-55af2d47d6bc", "24b4def4-5cf4-4e27-8b76-2edbd6932f86",
				"c16ec4da-67a0-4832-a52c-a8c53108a582", "e0d0bc2a-4b2a-4698-bd21-507eacadfd4c",
				"22c78cad-63bd-4ade-a652-a46f697036d0", "ad319cbf-2730-4367-99b1-5bbcc52f9dbd",
				"c42e1bcf-9791-4f00-b49a-f9496cae5266", "90f59707-b441-4cba-bd76-932e7305b201",
				"d00ca3ea-146e-4073-8624-6ef305affc5b", "4fc2fd06-9ea3-479a-8092-97e1f1327234",
				"98248010-94a4-4bee-94ef-033a86fbda83", "ed8bd00b-04e7-4e8a-acb4-0693e1b17c71",
				"c2660cee-47d2-49f6-a9a4-c5bfd3cab1c2", "7868acc5-ea8b-4ccd-afb8-196706c3e7aa",
				"e37ab643-4943-4b8b-a0d3-20cac37c1b2f", "f0a55e0c-a114-4040-a9ba-7c4bed7eb637",
				"035561e7-231e-4701-9cea-d51c6d5cb080", "0a52e96a-f680-4729-9dbd-f52476c5159f",
				"b4e49370-029c-4cfa-8940-8eb21191431e", "15aa9c7f-3d40-4a94-a56c-0c4836695b40",
				"f07e5321-0440-47ba-aa7b-a43e7aca3cc1", "99074891-01d0-4edc-afc2-043fe34d8e64",
				"2ca3a48e-9923-4b6e-a977-34c638fadc20", "9d2f35e3-cdb4-49f6-8617-3a6e17ea9ce5",
				"90e77b07-7115-4070-a182-035c8544ca54", "4648ac34-4a8e-40ec-89f0-b351632e2199",
				"6dc6b104-2c0a-4042-866d-277963de44e4", "70e6b56f-45eb-4e31-bc6e-dff3ab698b00",
				"2972313d-e058-4e5a-95ef-811e427f216f", "e36a3005-7cf0-4e82-8794-d17bfb85309c",
				"11963941-e647-457b-8039-2c59edc06b3a", "86f177c9-5004-420d-8382-dea60a39d55b",
				"946ddfb6-6065-4797-a344-e5550b48b6d9", "ceec51c0-aed2-4be8-8832-b10a8047fe3c",
				"1166c4a7-ebc5-4f4f-887b-ce3051f26654", "11e5dda1-6692-4f14-90fd-ab6b229eff2f",
				"3da880d9-141d-495c-addb-78419bc9931a", "5e522aba-5ba3-4674-ba39-6a2bc59b0e68",
				"77b3c79b-81f8-4e53-8539-a5c94cf83a42", "ec0e1704-ebe8-4cef-bd8c-f05cc01619b8",
				"9bf2de7f-31da-4b29-8585-a9f06ea7d8ab", "06d65afe-9fd2-471d-b513-4b626f3e62fd",
				"b47cfff2-cb9d-49e3-827b-847d987a74a1", "343a2ec8-2b9e-47f0-95b0-12306dbfa23c",
				"b47ef58e-f75f-466d-93b0-40965ec7d9fc", "374a502d-e825-4bc3-b542-03460e5abd8f",
				"61fff0c5-02ac-4696-bbdb-12c0bfe90480", "e67b7799-d25a-48b7-b953-2b26017fefbc",
				"5bbc227e-0cd8-46cf-a29d-64718da20358", "049976ae-1668-41f6-8b9c-3cdb9629d6a6",
				"d3325ee8-0fe1-4c60-b108-ca7ff14c724f", "c05d2cc3-5b3f-45d4-974c-30b735e1df7a",
				"9c62f8c8-0ce1-4b7f-8baa-facbcda33a89", "e6171f3a-4067-489f-baba-e5435cb8e405",
				"c43034a5-8b3f-464d-a59b-d377ad2e58d4", "dde70204-75dc-47d9-bb7c-21ed3ca68606",
				"08eda3b8-2c3b-4ccb-8bbc-a954930cb301", "24d62476-6802-4016-a865-a2e66b16479c",
				"ce5d1de2-78c6-4af2-a1c5-1244e3baabdb", "b4df9bc2-c88e-485e-8239-df1276cacc9a",
				"cfba926d-8936-4c0e-b54a-583985cf1d04", "8fdc760b-725b-45e7-a2cd-44c825b87c0c",
				"1ba03b8e-03c0-48b0-9397-da17514553cc", "c2244e7f-2b22-49ef-9b19-60aea6060548",
				"ac729eaa-1b8a-4090-a90d-22ba33742d0d", "c0fea6ad-f574-4781-8652-6a827479402b",
				"91d37b20-4360-4036-9ad0-d2c589ddadc4", "c11bf926-fa75-48a8-a4e7-74c82e55fb89",
				"be3a1e53-8854-43b4-a368-098e3b59b452", "2fdc8d15-a1fc-422b-b36f-8c357e0a570e",
				"74ec1a66-3361-4675-90ea-1713e90f6627", "78249583-8852-4b61-9b4c-c1ae533b7d5b",
				"2fb7ed1f-6ae9-4aba-be2f-a5a27f7e05f0", "d4b5723a-6850-453a-ab97-2c0ff2294a22",
				"e746f0f6-6182-42e8-9717-72113e8b7d27", "2d67a5d6-bce7-4e09-93c7-469a084fae31",
				"79ed4143-b9ba-4e5c-bcd5-89067af21eb6", "38cc079d-a36f-48df-b448-e7b3c0988365",
				"5574d9cf-1d9a-487e-8f6d-b0ee5777159f", "9b7ff9df-b2c2-456a-befd-6af608b778be",
				"4d7764ef-2e9a-4bdf-b6c0-b073ff3fa577", "e702df1d-2b48-4c49-8c1a-31ec80798605",
				"d9b6aa1a-e338-4adb-a8cd-964a628a56f8", "a8770cc2-ceb1-4415-94dd-fa6ffaa1acee",
				"933172cd-a094-4788-bd8e-ace334c54ad7", "3a11fbac-e231-4c85-a4d7-8a6c89c06a3f",
				"e7b79c75-d7b8-4df3-a3b8-a029ecce92d7", "c3a3b9e5-d362-41ff-852a-85bd44c1c8e9",
				"ca771bbd-ed36-4720-ac2d-454eaa01c578", "9142d76a-5321-4e8a-baa2-5f7749ba5e82",
				"aff17b4d-bbf1-4bbc-b53a-1d580a3c2317", "855bcfc2-29f4-43be-97e8-18e1a0d26c70",
				"55357644-e91f-4e4b-bc41-94b71b571326", "6ce85f3c-4b0b-4b83-a09b-3128a16af7d2",
				"c959736f-e149-415f-8bc4-322701874c4c", "fc27c6ed-c74e-424e-b2a5-7a6ea08c1c6a",
				"e025f6ef-960d-48ea-9ef5-00f318436704", "0b4016ce-77d9-41b2-a2f0-bb10622bd08f",
				"c71e455c-e2b5-48a4-9f2d-f4193a752e74", "6ff3a616-ce60-44ed-bebe-754bec593ab5",
				"79ce0843-159f-4807-8a87-c24708d7c34c", "27a4095b-c4cf-463d-a03b-4d9cb599fe5c",
				"e78eebd9-e97d-4a85-979d-631a83c58b9a", "e2c748cd-0b77-48c2-863e-cc1ee4f4af8a",
				"2d96f5ed-31c0-4005-927f-72b949b62e88", "d51f9d8a-713a-43f4-8600-b3665f0e39b8",
				"88e3478c-b861-4cd1-858c-5676bb098f09", "ff4bbb2e-4584-46c1-b9ac-c61427317557",
				"b87b7ff2-c362-4d6b-bcc2-ce87c537e4c4", "e26f68cb-9574-44f5-be20-6c6e92379fdc",
				"a65e1d76-c697-432b-947d-0235a3ffdee3", "d9bbb17c-c5b5-4d7a-ab6f-5b750ba2e1a7",
				"4206643c-8c0e-4a24-b2f9-da293fd4252c", "9ac2606e-346a-44e0-b99f-ddacc4be8ca1" };
		List<Question> questions = new ArrayList<Question>();
		for (int i = 0; i < 100; i++) {
			Question question = new Question(questionIds[i] + "_问题_" + i, "" + i);
			questions.add(question);
			for (int j = 0; j < 6; j++) {
				question.getOptions().add(new Option(optIds[j + (6 * i)] + "_选项_" + j, "" + j));
			}
		}
		return questions;
	}

	public static String md5(String plainText) {
		// return DigestUtils.md5Hex(plainText);
		// return
		// org.springframework.util.DigestUtils.md5DigestAsHex(plainText.getBytes(StandardCharsets.UTF_8));
		return getMd5(plainText);
	}

	public static String getMd5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuilder buf = new StringBuilder("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
			// 16位的加密
			// return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Data
	class Question {
		private String pid;
		private String orderIndex;
		private List<Option> options = new ArrayList<Option>();

		public Question(String pid, String orderIndex) {
			this.pid = pid;
			this.orderIndex = orderIndex;
		}
	}

	@Data
	class Option {
		private String pid;
		private String orderIndex;

		public Option(String pid, String orderIndex) {
			this.pid = pid;
			this.orderIndex = orderIndex;
		}
	}

}
