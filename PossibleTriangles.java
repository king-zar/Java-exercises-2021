// Kinga Zarska - 8
/*
 * Celem programu jest zastosowanie efektywnego algorytmu, ktory wyznaczy liczbe mozliwych trojek,
 * ktore beda mogly utworzyc trojkaty sposrod wartosci zawartych we wczytywanej tabeli. Algorytm pozwala
 * na zredukowanie zlozonosci z O(n^3) do O(n^2).
 * Zredukownanie zlozonosci jest mozliwe, poniewaz do zastosowania algorytmu wykorzystujemy w metodzie 2 petle
 * for i while, for przechodzi od elementu o indeksie n-1 do elementu o indeksie 1, z kolei while obejmuje elementy
 * od indeksu 0 do indeksu o 1 mniejszego niz ten, na ktory wskazuje iterator funkcji while, wiadomo, ze zlozonosc
 * dwoch zagniezdzonych petli jest rowna iloczynowi liczby ich iteracji, zatem w naszym wypadku zlozonosc wyniesie
 * maksymalnie n^2, tzn. O(n^2)
 * */
import java.util.Scanner;

class Source { // glowna klasa
    public static Scanner scan = new Scanner(System.in); // zmienna klasy Scanner dla wejscia

    public static void main(String args[]) {
        int numbOfSets = scan.nextInt(); // zmienna przechowuje ilosc wczytywanych zestawow


        for (int i=0; i<numbOfSets; i++) { // analize robimy dla kazdego zestawu z osobna
            int numbOfSections = scan.nextInt(); // zmienna zawiera informacje o ilosci odcinkow w danym zestawie
            int[] sectionLengths = new int[numbOfSections]; // tablica wartosci dlugosci rozwazanych odcinkow

            for (int j=0; j<numbOfSections; j++) { // wczytywanie danych do tablicy
                sectionLengths[j] = scan.nextInt();
            }

            /*
             * na ekran wypisujemy liczbe mozliwych trojkatow przy danych odcinkach korzystajac z metody
             * NumberOfTriangles, ktora umozliwia policzenie tej liczby
             */
            System.out.println("Num_triangles= " + NumberOfTriangles(sectionLengths, numbOfSections));
        }
    }

    /*
     * metoda pozwoli na wyliczenie liczby mozliwych trojkatow, values bedzie tablica dlugosci odcinkow
     * przekazywana do metody, natomiast n - dlugoscia tej tablicy, czyli iloscia odcinkow
     */
    static int NumberOfTriangles(int[] values, int n) { // metoda pozwoli na wyliczenie liczby mozliwych trojkatow
        if (n<3) { // jezeli nie podano min. 3 odcinkow, nie uzyskamy trojkata
            return 0;
        }

        int sum = 0; // zmienna pozwoli nam na zliczanie ilosci mozliwych kombinacji odcinkow

        // algorytm wymaga od nas posortowania tablicy tak, aby byla posortowana niemalejaco
        for (int i=0; i<n; i++) { // idziemy w petli od pierwszego elementu, czyli tego o indeksie 0
            int tmp = 0; // zmienna pomocnicza, ktora bedzie pamietac zmieniana wartosc
            for (int j=1; j<n; j++) { // zapamietujemy nastepny element
                if (values[j-1]>values[j]) { // porownujemy element poprzedni z nastepnym i jezeli poprzedni jest wiekszy
                    tmp = values[j-1]; // zapamietujemy poprzedni element
                    values[j-1] = values[j]; // wstawiamy w jego miejsce element nastepny
                    values[j] = tmp;
                    // dzieki zmiennej pomocniczej stary element nastepny zostaje nadpisany wartoscia elementu poprzedniego
                }
            }
        }

        /* ponizszy kod dziala juz na posortowanej tablicy
         * i to indeks ostatniego elementu, wraz z kolejnymi iteracjami przesuwamy sie w tabeli w lewo po indeksach
         */
        for (int i=n-1; i>=1; i--) {
            int left = 0; // indeks w tablicy najbardziej po lewej
            int right = i-1; // indeks w tablicy jeden wczesniej niz i
            while (left < right) { // gdy indeksy sie spotykaja, przerywamy petle
                /*
                 * musi zostac spelniony warunek trojkata, tzn. ze suma dlugosci dwoch krotszych bokow
                 * jest wieksza niz dlugosc najdluzszego boku
                 * jezeli warunek ten zachodzi dla elementu o indeksie right, to zajdzie tez dla elementu
                 * right-1, right-2 itd. z racji, ze tablica jest posortowana, wiec liczba trojkatow mozliwa
                 * do utworzenia dla danych left, right oraz i jest rowna dokladnie roznicy miedzy indeksami
                 * right oraz left
                 */
                if (values[right] + values[left] > values[i]) {
                    sum += right - left;
                    /*
                     * skoro mamy trojkat dla elementu o indeksie right, to przesuwamy sie indeksem right
                     * w lewo, czyli do wartosci mniejszej lub rownej poprzedniej, i w nastepnym wykonaniu petli
                     * while sprawdzamy, czy przy zmniejszeniu right suma krotszych bedzie nadal
                     * wieksza od najdluzszego, czyli szukamy wiecej mozliwych trojek tworzacych trojkat
                     */
                    right--;
                } else {
                    /*
                     * skoro weszlismy do else, to suma krotszych jest mniejsza niz najdluzszego, a zatem
                     * nie utworzyl nam sie trojkat, w takim razie przesuwamy indeks left dalej w prawo w tabeli,
                     * aby osiagnal on wartosc wieksza lub rowna poprzedniej i w nastepnym wykonaniu
                     * petli while sprawdzimy, czy takie zwiekszenie da nam trojkat, czyli czy suma krotszych bedzie
                     * wieksza od najdluzszego
                     */
                    left++;
                }
            }
        }

        return sum; // zwracamy wyliczona liczbe trojkatow
    }

}

/*
--- input 1 ---
5
7
1 1 1 1 1 1 1
2
1 2
3
2 2 2
4
1 1 2 3
4
2 3 4 5

--- output 1 ---
Num_triangles= 35
Num_triangles= 0
Num_triangles= 1
Num_triangles= 0
Num_triangles= 3
 */