package EntertainmentBot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SortBy {

	public SortBy() {

	}

	public static HashMap<String, Integer> getWordCount(String text) {
		return (HashMap<String, Integer>) Arrays.stream(text.toLowerCase().split(" "))
				.collect(Collectors.toMap(x -> x, x -> 1, (a, b) -> a + b));
	}

	public static String onlyAlpha(String text) {
		return text.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
	}
	
	public static Entertainment[] filterByDate(Entertainment[] entertainment, String date) {
		ArrayList<Entertainment> list = (ArrayList<Entertainment>) Arrays.stream(entertainment).filter(x -> x.getYear().equals(date)).collect(Collectors.toList());
		return list.toArray(new Entertainment[list.size()]);
		
	}

	public static Entertainment[] sortRelevance(Entertainment[] entertainment, String query) {
		HashMap<Entertainment, Integer> rScoreMap = new HashMap<>();
		for (Entertainment e : entertainment) {
			String eName = onlyAlpha(e.getName());
			String qName = onlyAlpha(query);
			int rScore = 0;
			HashMap<String, Integer> eWordCount = getWordCount(eName);
			HashMap<String, Integer> qWordCount = getWordCount(qName);
			boolean contains = false;
			for (String w : qWordCount.keySet()) {
				if (eWordCount.containsKey(w)) {
					contains = true;
					rScore += (-1 * Math.abs((qWordCount.get(w) - eWordCount.get(w))));
				} else {
					rScore += -1;
				}
			}

			if (!contains) {
				rScoreMap.put(e, rScore + -1000);
			} else {
				rScore += (Math.abs(qWordCount.size() - eWordCount.size()) * -1);
				String[] qWords = qName.split(" ");
				String[] eWords = eName.split(" ");
				for (int i = 0; i < qWords.length; i++) {
					if (i < eWords.length) {
						rScore += (eWords[i].contains(qWords[i]) ? 0 : -1);
					} else {
						break;
					}
				}
				rScoreMap.put(e, rScore);
			}
		}

		System.out.println("=====================");

		HashMap<Entertainment, Integer> relevant = (HashMap<Entertainment, Integer>) rScoreMap.entrySet().stream()
				.filter(x -> x.getValue() > -1000).sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
		relevant.forEach((a, b) -> System.out.println(a.getName() + ": " + b));

		System.out.println("---------------------");
		HashMap<Entertainment, Integer> asside = (HashMap<Entertainment, Integer>) rScoreMap.entrySet().stream()
				.filter(x -> x.getValue() <= -1000).sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
		// .forEach((a, b) -> System.out.println(a.getName() + ": " + b));

		ArrayList<Entertainment> relevantList = new ArrayList<>();

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		for (Integer x : relevant.values().stream().collect(Collectors.toSet())) {
			if (x == 0) {
				relevant.entrySet().stream().filter(a -> a.getValue().equals(x))
						.sorted((b, c) -> Integer.compare(c.getKey().getYearInt(), b.getKey().getYearInt()))
						.forEach(h -> relevantList.add(h.getKey()));
			} else {
				relevant.entrySet().stream().filter(a -> a.getValue().equals(x))
						.collect(Collectors.toMap(b -> b.getKey(),
								b -> onlyAlpha(query).compareTo(onlyAlpha(b.getKey().getName()))))
						.entrySet().stream()
						.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors
								.toMap(Map.Entry::getKey, Map.Entry::getValue, (g, h) -> g, LinkedHashMap::new))
						.forEach((o, p) -> relevantList.add(o));
			}
			// System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLL");
			// .keySet().forEach(p -> System.out.println(p.getName()));
		}

		asside.entrySet().stream()
				.sorted((a, b) -> onlyAlpha(a.getKey().getName()).compareTo(onlyAlpha(b.getKey().getName())))
				.forEach(h -> relevantList.add(h.getKey()));

		System.out.println("Size: " + relevantList.size());
		relevantList.forEach(p -> System.out.println(p.getName() + ": " + p.getYear()));

		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

		return relevantList.toArray(new Entertainment[relevantList.size()]);
	}
}
