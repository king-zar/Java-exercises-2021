/*
* Celem programu jest rozwiazanie problemu poszukiwania podtablicy, ktorej suma elementow bedzie najwieksza.
* Jezeli elementy kilku tablic sumuja sie do takiego samego najwiekszego wyniku, wybieramy te tablice,
* ktora zawiera mniej elementow.
* Jezeli tablice maja zarowno takie same sumy oraz identyczna liczbe elementow, bierzemy pod uwage porzadek
* leksykograficzny.
* Do implementacji wykorzystalam algorytm Kadane.
* */
import java.util.Scanner;

class Source { // glowna klasa
    public static Scanner scan = new Scanner(System.in); // zmienna klasy Scanner dla wejscia

    public static void main (String [] args) {
        int numbOfSets = scan.nextInt(); // zmienna przechowuje ilosc wczytywanych zestawow

        for (int k = 0; k<numbOfSets; k++) { // analize robimy dla kazdego zestawu z osobna
            int set = scan.nextInt(); // numer zestawu
            String data = scan.next(); // odczytamy znak ":" ktory "przeszkadza" nam w czytaniu kolejnego int
            int numbOfRows = scan.nextInt(); // liczba wierszy wszytywanej tablicy
            int numbOfColumns = scan.nextInt(); // liczba kolumn wczytywanej tablicy
            // ponizsza tablica przechowuje zestaw o numerze = set, a jej wymiary sa zgodne z wejsciowymi*/
            int[][] mainTable = new int[numbOfRows][numbOfColumns];
            boolean minusNumb = true; // zmienna pomocnicza dla tablicy o samych ujemnych elementach
            boolean zeroNumb = true; // zmienna pomocnicza dla tablicy o samych zerowych elementach

            /*
            * wzor do obliczenia sumy:
            * ms(i, j, k, l) = 3*D+2*U, gdzie D - suma elementow dodatnich, U - suma elementow ujemnych
            * W ponizszej petli zwiekszamy kolejne wczytywane elementy tablicy (parzyste trzykrotnie, nieparzyste
            * dwukrotnie) i tak zmienione wstawiamy do tablicy mainTable.
             */
            for (int j = 0; j < numbOfRows; j++) {
                for (int i = 0; i < numbOfColumns; i++) {
                    int nextElement = scan.nextInt();
                    if (nextElement > 0) {
                        mainTable[j][i] = 3 * nextElement;
                        minusNumb = false; // false oznacza, ze tablica nie ma samych ujemnych elementow
                        zeroNumb = false; // false oznacza, ze tablica nie ma samych zerowych elementow
                    }

                    if (nextElement < 0) {
                        mainTable[j][i] = 2 * nextElement;
                        zeroNumb = false; // false oznacza, ze tablica nie ma samych zerowych elementow
                    }

                    if (nextElement == 0) {
                        mainTable[j][i] = 0; // dla 0 nie zwiekszamy wartosci, bo nie jest to potrzebne (0 * x = 0)
                        minusNumb = false; // false oznacza, ze tablica nie ma samych ujemnych elementow
                    }
                }
            }

            if (minusNumb == true) { // gdy wszystkie podane wartosci < 0 nie liczymy sumy, tylko od razu wypisujemy
                System.out.println(set + ": n=" + numbOfRows + " m=" + numbOfColumns + ", ms= 0"
                        + ", mstab is empty");
                continue; // przechodzimy do nastepnego zestawu
            }

            if (zeroNumb == true) { // gdy wszystkie podane wartosci = 0 nie liczymy sumy, tylko od razu wypisujemy
                System.out.println(set + ": n=" + numbOfRows + " m=" + numbOfColumns + ", ms= 0"
                        + ", mstab= a[0..0][0..0]");
                continue; // przechodzimy do nastepnego zestawu
            }

            // wywolanie metody liczacej najwieksza sume
            maxSubarray(set, numbOfRows, numbOfColumns, mainTable);
        }
    }

    public static void maxSubarray (int set, int rowsNumber, int columnsNumber, int[][] array) {
        /*
        * dodatkowa tablica potrzebna do implementacji algorytmu, bedzie przechowywac zsumowane wartosci wierszy
        * dla kolumn w zakresie okreslonym przez indeksy l od left i r od right
         */
        int[] sumTable = new int[rowsNumber];
        int maxSum = 0; // to bedzie nasz ostateczny wynik
        int left = 0; // indeks kolumny maksymalnej podtablicy najbardziej po lewej
        int right = 0; // indeks kolumny maksymalnej podtablicy najbardziej po prawej
        int up = 0; // indeks najwyzszego wiersza maksymalnej podtablicy
        int down = 0; // indeks najnizszego wiersza maksymalnej podtablicy
        int currentSum = 0; // suma dla aktualnie rozwazanej podtablicy
        int startingPoint = 0; // numer wiersza od ktorego nastepuje przegladanie wartosci
        int zeroRow = -1; // indeks wiersza dla 1 napotkanego 0
        int zeroColumn = -1; // indeks kolumny dla 1 napotkanego 0
        int widthMax = 0; // szerokosc podtablicy o najwiekszej sumie
        int highMax = 0; // wysokosc podtablicy o najwiekszej sumie
        int width = 0; // szerokosc aktualnie rozwazanej podtablicy
        int high = 0; // wysokosc aktualnie rozwazanej podtablicy

        /*
        *l - left, petla zaczyna sie od kolumny startowej, na poczatek 0, ktora wraz z postepem algorytmu bedzie
        * przesuwac sie coraz bardziej w prawa strone
        */
        for (int l=0; l<columnsNumber; l++) {
            for (int i=0; i<rowsNumber; i++) {
                sumTable[i] = 0; // wypelniamy pomocnicza tablice zerami
            }

            /*
             * r - right; kolumna koncowa podtabeli, ktora przesuwa sie podczas poprzedniego fora, dla kazdego l
             * musimy rozwazyc wszystkie wartosci r >= l, tzn. wszystkie podtabele zlozone z kolumn rozpoczynajac
             * od l az do r
             */
            for (int r=l; r<columnsNumber; r++) {
                for (int j=0; j<rowsNumber; j++) {
                    sumTable[j] += array[j][r]; // sumujemy wartosci dla odpowiednich wierszy dla danej podtabeli
                }

                for (int j=0; j<rowsNumber; j++) {
                    /*
                     * zliczamy wszystkie wartosci pomocniczej tabeli rozpoczynajac od elementu o indeksie j,
                     * czyli rozwazamy sume kazdej mozliwej tablicy, "zmniejszajac" wielkosc tej tablicy
                     * wraz ze wzrostem j
                     */
                    currentSum += sumTable[j];

                    if (currentSum <= 0) {
                        currentSum = 0; // ujemne sumy nas nie interesuja

                        startingPoint = j + 1; // zmniejszamy wielkosc podtablicy, wybierajac juz mniej wierszy
                    }

                    /*
                     * if zapisuje indeks wiersza i kolumny pierwszego napotkanego 0 (dla przypadku,
                     * gdzie tablica wejciowa zawiera jedynie wymieszane elementy ujemne i zerowe
                     */
                    if (currentSum == 0 && zeroColumn == -1 && sumTable[j] == 0) {
                        zeroColumn = r;
                        zeroRow = j;
                    }

                    width = r - l + 1; // szerokosc rozwazanej podtabeli wyliczana z indeksow
                    high = j - startingPoint + 1; // wysokosc rozwazanej podtabeli wyliczana z indeksow

                    /*
                     * przypisujemy nowo znaleziona najwieksza sume jako maksymalna oraz punkty, ktore pozwola nam
                     * na odnalezienie pozniej naszej podtabeli, a takze nowe wymiary
                     */
                    if (maxSum < currentSum) {
                        maxSum = currentSum;
                        left = l;
                        right = r;
                        up = startingPoint;
                        down = j;
                        widthMax = width;
                        highMax = high;
                    }

                    if (maxSum == currentSum) { // przypadek gdy aktualna suma jest rowna sumie maksymalnej
                        if (width*high < widthMax*highMax) { // wybiera te podtabele, ktora zawiera mniej elementow
                            left = l;
                            right = r;
                            up = startingPoint;
                            down = j;
                            widthMax = width;
                            highMax = high;
                        }

                        /*
                         * gdy liczba elementow jest taka sama, bierze pod uwage porzadek leksykograficzny; najpierw
                         * po najwyzszym indeksie wiersza, potem najnizszym indeksie wiersza, indeksie kolumny
                         * najbardziej na lewo, a na koncu indeksie wiersza najbardziej na prawo
                         */
                        if (width*high == widthMax*highMax) {
                            if (startingPoint < up) {
                                left = l;
                                right = r;
                                up = startingPoint;
                                down = j;
                            }
                            if (startingPoint == up) {
                                if (j < down) {
                                    left = l;
                                    right = r;
                                    down = j;
                                }
                                if (j == down) {
                                    if (l < left) {
                                        left = l;
                                        right = r;
                                    }
                                    if (l == left) {
                                        if (r < right) {
                                            right = r;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                /*
                 * ustawienie wartosci wejsciowych, aby moc policzyc sumy kolejnych podtablic, tym razem
                 * zaczynajacych sie od kolumny o indeksie l+1
                 */
                currentSum = 0;
                startingPoint = 0;
            }
        }

        if (maxSum == 0) { // gdy najwieksza suma = 0 bierzemy indeksy pierwszego spotkanego 0
            System.out.println(set + ": n=" + rowsNumber + " m=" + columnsNumber + ", ms= " + maxSum
                    + ", mstab= a[" + zeroRow + ".." + zeroRow + "][" + zeroColumn + ".." + zeroColumn + "]");
        } else { // jesli suma niezerowa, wypisujemy normalnie
            System.out.println(set + ": n=" + rowsNumber + " m=" + columnsNumber + ", ms= " + maxSum
                    + ", mstab= a[" + up + ".." + down + "][" + left + ".." + right + "]");
        }
    }
}

/*
--- input 1 ---
3
1 : 3 3
6 -5 -7
-9 3 -6
-10 4 7
2 : 2 2
0 0
0 0
3 : 2 3
-1 -1 0
-2 -3 -4

--- output 1 ---
1: n=3 m=3, ms= 33, mstab= a[2..2][1..2]
2: n=2 m=2, ms= 0, mstab= a[0..0][0..0]
3: n=2 m=3, ms= 0, mstab= a[0..0][2..2]

--- input 2 ---
3
1 : 3 4
-1 -1 -1 -1
-1 -1 -1 -1
-1 1 -1 -1
2 : 2 2
-2 -3
-4 -5
3 : 1 3
3 4 5

--- output 2 ---
1: n=3 m=4, ms= 3, mstab= a[2..2][1..1]
2: n=2 m=2, ms= 0, mstab is empty
3: n=1 m=3, ms= 36, mstab= a[0..0][0..2]

--- Jako input 3 zostal uzyty przyklad podany w opisie zadania.
*/
