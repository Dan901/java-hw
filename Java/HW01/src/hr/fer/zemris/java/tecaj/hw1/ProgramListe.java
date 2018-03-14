package hr.fer.zemris.java.tecaj.hw1;

/**
 * Implementation and demonstration of a singly linked list.
 * 
 * @author Dan
 *
 */
public class ProgramListe {

	/**
	 * Node of the list.
	 * 
	 * @author Dan
	 *
	 */
	static class CvorListe {
		CvorListe sljedeci;
		String podatak;
	}

	/**
	 * List demonstration.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CvorListe cvor = null;
		cvor = ubaci(cvor, "Jasna");
		cvor = ubaci(cvor, "Ana");
		cvor = ubaci(cvor, "Ivana");
		System.out.println("Ispisujem listu uz originalni poredak:");
		ispisiListu(cvor);
		cvor = sortirajListu(cvor);
		System.out.println("Ispisujem listu nakon sortiranja:");
		ispisiListu(cvor);
		int vel = velicinaListe(cvor);
		System.out.println("Lista sadrzi elemenata: " + vel);
	}

	/**
	 * Calculates length of the list.
	 * 
	 * @param cvor
	 *            Head of the list.
	 * @return Length of the list.
	 */
	private static int velicinaListe(CvorListe cvor) {
		if (cvor == null) {
			return 0;
		}
		return 1 + velicinaListe(cvor.sljedeci);
	}

	/**
	 * Adds one element to the end of the list.
	 * 
	 * @param prvi
	 *            Head of the list.
	 * @param podatak
	 *            Value of the new node.
	 * @return Head of the list.
	 */
	private static CvorListe ubaci(CvorListe prvi, String podatak) {
		if (prvi == null) {
			CvorListe cvor = new CvorListe();
			cvor.podatak = podatak;
			prvi = cvor;
		} else {
			prvi.sljedeci = ubaci(prvi.sljedeci, podatak);
		}
		return prvi;
	}

	/**
	 * Prints the whole list to {@code System.out}.
	 * 
	 * @param cvor
	 *            Head of the list.
	 */
	private static void ispisiListu(CvorListe cvor) {
		if (cvor != null) {
			System.out.println(cvor.podatak);
			ispisiListu(cvor.sljedeci);
		}
	}

	/**
	 * Sorts the list in alphabetic order.
	 * 
	 * @param cvor
	 *            Head of the list.
	 * @return Head of the list.
	 */
	private static CvorListe sortirajListu(CvorListe cvor) {
		if (velicinaListe(cvor) < 2) {
			return cvor;
		}

		CvorListe first = cvor;
		boolean sorted;
		do {
			sorted = true;
			while (cvor != null) {
				if (cvor.sljedeci != null && cvor.podatak.compareTo(cvor.sljedeci.podatak) > 0) {
					String value = cvor.podatak;
					cvor.podatak = cvor.sljedeci.podatak;
					cvor.sljedeci.podatak = value;
					sorted = false;
				}
				cvor = cvor.sljedeci;
			}
		} while (!sorted);

		return first;
	}

}
