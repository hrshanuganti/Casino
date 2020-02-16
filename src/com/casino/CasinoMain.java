package com.casino;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class CasinoMain {

	Timer timer;
	Map<String, Map<String, Double>> names = new HashMap<>();
	Map<String, Double> numbers = new HashMap<>();
	boolean running = true;
	Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		CasinoMain main = new CasinoMain();
		main.initiateRollet();
		main.startRollet(30);
	}

	public void initiateRollet() {
		InputStream stream = CasinoMain.class.getResourceAsStream("SomeTextFile.txt");
		List<String> collect = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.toList());
		collect.stream().forEach(e -> {
			Map<String, Double> numbers = new HashMap<>();
			numbers.put("EVEN", 0.0);
			numbers.put("ODD", 0.0);
			for (int i = 1; i <= 36; i++) {
				numbers.put(i + "", 0.0);
			}
			names.put(e, numbers);
		});
	}

	public void startRollet(int seconds) {
		timer = new Timer();
		timer.schedule(new RemindTask(), seconds * 1000);
		System.out.println("Started Rollet");
		while (running) {
			try {
				names.entrySet().stream().forEach(e -> {
					System.out.println(e.getKey() + " Bets please");
					String numberAmount = sc.nextLine();
					System.out.println("Name, Number, Amount " + e.getKey() + " " + numberAmount);
					String[] split = numberAmount.split(" ");

					Double amount = e.getValue().get(split[0]);
					if (amount != null) {
						amount = amount + Double.parseDouble(split[1]);
						e.getValue().put(split[0], amount);
					}

				});

			} catch (IllegalStateException e) {
			}
		}
	}

	Random random = new Random();

	class RemindTask extends TimerTask {
		public void run() {
			running = false;
			sc.close();
			System.out.println("Bets Closed");
			timer.cancel();
//			names.forEach((k, v) -> System.out.println((k + ":" + v)));
			int number = random.nextInt(35) + 1;
			// number = 4;
			System.out.println("===========================");
			System.out.println("Number: " + number);
			System.out.println("Player" + "\t" + "Bet" + "\t\t\t" + "outcome" + "\t\t" + "winnings");
			calculateBets(number);
		}
	}

	private void calculateBets(int number) {

		names.entrySet().stream().forEach(e -> {

			Double amount = e.getValue().get(number + "");
			String outcome = "LOSE";

			if (amount != null && amount.doubleValue() > 0.0) {
				amount = amount * 36;
				outcome = "WIN";
			}

			if ((number % 2) == 0) {
				Double evenNum = e.getValue().get("EVEN");
				if (evenNum != null && evenNum.doubleValue() > 0.0) {
					amount = evenNum * 2 + amount;
					outcome = "WIN";
				}
			} else {
				Double oddNum = e.getValue().get("ODD");
				if (oddNum != null && oddNum.doubleValue() > 0.0) {
					amount = oddNum * 2 + amount;
					outcome = "WIN";
				}
			}

			StringBuilder bet = new StringBuilder();

			e.getValue().entrySet().stream().forEach(v -> {
				if (v.getValue() > 0.0) {
					bet.append(v.getKey() + ",");
				}
			});

			System.out.println(e.getKey() + "\t" + bet + "\t\t\t" + outcome + "\t\t" + amount);
		});

		System.out.println("===========================");

	}

}
