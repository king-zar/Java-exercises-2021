/*
 * Program umozliwia rozwiazanie problemu pakowania plecaka, tzn. znajduje sekwencje elementow, ktorych
 * sumaryczna waga jest rowna maksymalnej pojemnosci plecaka.
 * W tym celu wykorzystuje dwie funkcje - rekurencyjna oraz iteracyjna.
 */
import java.util.Scanner;

class Source {
    public static Scanner scan = new Scanner(System.in); // zmienna klasy Scanner dla wejscia
    public static boolean find = false; // umozliwi zakonczenie funkcji po znalezieniu pierwszej szukanej sekwencji
    public static int[] extraTab; // dodatkowa tablica, ktora przechowuje 0 i 1

    public static void main (String[] args) {
        int numbOfSets = scan.nextInt(); // zmienna przechowuje ilosc zestawow danych

        for (int i=0; i<numbOfSets; i++) { // dla kazdego zestawu osobno
            int capacity = scan.nextInt(); // zmienna przechowuje pojemnosc plecaka
            int elemNumb = scan.nextInt(); // zmienna przechowuje liczbe elementow, ktore moga wypelnic plecak
            int[] Elem = new int[elemNumb]; // tablica z wagami kolejnych elementow
            extraTab = new int[elemNumb]; // tablica takiej samej wielkosci jak tablica wag

            for (int j=0; j<elemNumb; j++) { // zapisujemy do tablicy wagi kolejnych elementow
                Elem[j] = scan.nextInt();
            }

            recursive(Elem, capacity, 0); // wywolanie funkcji rekurencyjnej

            if (find == false) { // jezeli nie znalezlismy pasujacej sekwencji
                System.out.println("BRAK");

            } else { // znalezlismy pasujaca sekwencje
                String output = "REC: " + capacity + " =";
                for (int j = 0; j < extraTab.length; j++) {
                    // pobieramy do outputu tylko te elementy, dla ktorych element w dodatkowej tablicy
                    // o tym samym indeksie ma wartosc 1
                    if (extraTab[j] == 1) {
                        output += " " + Elem[j];
                    }
                }
                System.out.println(output);
                find = false; // zmienna pomocnicza na false (w funkcji iteracyjnej nie znalezlismy jeszcze sekwencji)
                iterative(Elem, capacity); // wywolanie funkcji iteracyjnej
            }
            find = false; // zerujemy dla kolejnej iteracji petli
        }
    }

    /*
     * Algorytm rekurencyjny:
     * Tab - tablica elementow z wagami
     * tempSum - na poczatku rowne pojemnosci plecaka (chcemy, aby osiagnelo 0)
     * index - indeks elementu w tablicy
     */
    public static void recursive (int[] Tab, int tempSum, int index) {
        if (tempSum == 0) { // znalezlismy sekwencje
            find = true;
            return;
        }
        // gdy suma mniejsza od zera lub tablica sie "skonczyla" - wracamy (return)
        if (tempSum < 0 || index == Tab.length) {
            return;
        }

        extraTab[index] = 1; // ustawiamy 1 dla obecnie rozwazanego elementu
        tempSum -= Tab[index]; // odejmujemy wage tego elementu od tempSum
        recursive(Tab, tempSum, index + 1); // rekurencja - wywolujemy biorac obecna tempSum i indeks wiekszy o 1

        if (find == true) { // znalezlismy sekwencje - konczymy program
            return;
        }

        // nie znalezlismy sekwencji i wyszlismy z rekurencji - wracamy tutaj
        extraTab[index] = 0; // zmieniamy indeks obecnie rozwazanego elementu
        tempSum += Tab[index]; // zwiekszamy tempSum o jego wage
        recursive(Tab, tempSum, index + 1); // rekurencja - wywolujemy biorac obecna tempSum i indeks wiekszy o 1

        return;
    }

    /*
     * Algorytm iteracyjny
     * Tab - tablica elementow z wagami
     * weight - pojemnosc plecaka
     */
    public static void iterative (int[] Tab, int weight) {
        int tempSum = weight; // na poczatku rowne pojemnosci plecaka
        IndexStack Index = new IndexStack(Tab.length); // tworzymy stos dla indeksow (rozmiar rowny ilosci elementow)
        int index; // przechowuje indeks
        Index.push(0); // wrzucamy pierwszy indeks na stos, czyli 0
        tempSum -= Tab[0]; // odejmujemy wage pierwszego elementu od tempSum

        while (!find) { // dopoki nie znalezlismy sekwencji
            if (tempSum == 0) { // sekwencja znaleziona
                String output = "";
                // dopoki stos nie jest pusty, sciagamy indeksy i wypisujemy elementy na wyjscie
                while (!Index.isempty()) {
                    output = Tab[Index.pop()] + " " + output;
                }
                System.out.println("ITER: " + weight + " = " + output);
                find = true; // zmieniamy na true, bo znaleziona

            } else {
                // gdy suma mniejsza od zera lub indeks na stosie jest rowny ostatniemy indeksowi w tabeli
                if (tempSum < 0 || Index.top() == Tab.length-1) {
                    index = Index.pop(); // sciagamy indeks ze stosu
                    tempSum += Tab[index]; // dodajemy wage elementu o tym indeksie do tempSum

                    if (index == Tab.length-1) { // indeks na stosie jest rowny ostatniemy indeksowi w tabeli
                        index = Index.pop(); // sciagamy indeks ze stosu
                        tempSum += Tab[index]; // dodajemy wage elementu o tym indeksie do tempSum
                    }

                    if (!find) { // gdy nie znalezlismy
                        Index.push(index+1); // wrzucamy na stos kolejny indeks
                        tempSum -= Tab[Index.top()]; // odejmujemy wage tego elementu od tempSum
                    }
                } else {
                    Index.push(Index.top()+1); // wrzucamy indeks nastepnego elementu na stos
                    tempSum -= Tab[Index.top()]; // odejmujemy wage tego elementu od tempSum
                }
            }
        }
    }
}

class IndexStack {
    private int maxSize; // maksymalny rozmiar tablicy zwierajacej stos
    private int[] indexArray; // tablica zawierajaca indeksy wkladane na stos
    private int top; // indeks szczytu stosu

    public IndexStack (int size) { // konstruktor - Create()
        maxSize = size; // ustawiamy rozmiar tablicy
        indexArray = new int[maxSize]; // tworzymy tablice dla elementow
        top = -1; // na razie brak elementow (rosnie w gore)
    }

    public void push(int x) { // wstawianie element na szczyt stosu
        indexArray[++top] = x;
    }

    public int pop() { // usuwanie elementu ze szczytu stosu
        return indexArray[top--];
    }

    public int top() { // czyta element z wierzcholka stosu
        return indexArray[top];
    }

    public boolean isempty() { // zwraca true, jezeli stos pusty, false jesli cos jest na stosie
        if (top<0) {
            return true;
        }
        return false;
    }

}

/* ---- input ----
10
13
5
7 4 5 4 2
30
6
5 8 10 2 9 1
12
4
2 4 2 3
21
5
2 7 7 3 4
15
5
2 3 1 7 8
24
7
6 8 3 4 5 2 1
51
8
4 7 9 5 3 9 8 8
33
6
1 10 11 12 6 5
19
5
1 5 4 5 5
23
3
11 7 6
*/

/* ---- output ----
REC: 13 = 7 4 2
ITER: 13 = 7 4 2
REC: 30 = 8 10 2 9 1
ITER: 30 = 8 10 2 9 1
BRAK
REC: 21 = 7 7 3 4
ITER: 21 = 7 7 3 4
REC: 15 = 7 8
ITER: 15 = 7 8
REC: 24 = 6 8 3 4 2 1
ITER: 24 = 6 8 3 4 2 1
BRAK
REC: 33 = 1 10 11 6 5
ITER: 33 = 1 10 11 6 5
REC: 19 = 5 4 5 5
ITER: 19 = 5 4 5 5
BRAK
 */