// Kinga Zarska - 8
/*
 * program ma za zadanie przeprowadzac konwersje miedzy notacjami ONP i INF w obie strony, zakladamy na poczatku, ze
 * wyrazenia w obu notacjach moga byc niepoprawne, wiec przed przystapieniem do konwersji nalezy te poprawnosc
 * sprawdzic
 */
import java.util.Scanner;

class Source {
    public static Scanner scan = new Scanner(System.in); // zmienna klasy Scanner dla wejscia

    public static void main(String[] args) {
        int numbOfLines = scan.nextInt(); // zmienna przechowuje ilosc wczytywanych linii

        for(int i=0; i<numbOfLines; i++) { // osobno dla kazdej podanej linii
            String expressionType = scan.next(); // wczytanie rodzaju notacji (ONP: lub INF:)
            String expression = scan.nextLine(); // wczytanie linii z wyrazeniem w notacji ONP lub INF

            if (expressionType.equals("INF:")) { // jezeli podane bylo INF, zamieniamy na ONP
                // bierzemy tylko znaki dopuszczalne w notacji INF
                expression = expression.replaceAll("[^a-z+\\-=()!~^*/%<>?&|]","");
                String[] litery = expression.split(""); // tablica przechowuje kolejne znaki

                if (INFisCorrect(litery)) { // jezeli notacja INF poprawna
                    INFtoONP(litery); // dokonujemy konwersji do ONP
                } else { // jezeli notacja INF niepoprawna
                    System.out.println("ONP: error");
                }
            }

            if (expressionType.equals("ONP:")) { // jezeli podane bylo ONP, zamieniamy na INF
                // bierzemy tylko znaki dopuszczalne w notacji ONP
                expression = expression.replaceAll("[^a-z+\\-=!~^*/%<>?&|]","");
                String[] litery = expression.split(""); // tablica przechowuje kolejne znaki

                if (ONPisCorrect(litery)) { // jezeli notacja ONP poprawna
                    ONPtoINF(litery); // dokonujemy konwersji na INF
                } else { // jezeli notacja ONP niepoprawna
                    System.out.println("INF: error");
                }
            }
        }
    }

    public static boolean INFisCorrect (String tab[]) {
        // warunek 2b reguluje ponizsza implementacja, nie trzeba dodawac dodatkowych warunkow
        // zwraca false, gdy wyrazenie niepoprawne i true, gdy poprawne
        int pos = 0; // automat skonczony rozpoczyna analize na pozycji 0
        int n = tab.length; // ilosc elementow (operandow i operatorow) w danym wyrazeniu
        int bracket = 0; // zmienna do sprawdzania zgodnosci liczby nawiasow

        for (int i=0; i<n; i++) { // implementacja automatu skonczonego zgodnie z grafem z instrukcji
            if (pos==0) {
                if (priority(tab[i])==7) { // gdy operator jest jednoargumentowy
                    pos = 2;
                } else if (priority(tab[i])==10) { // gdy operand
                    pos = 1;
                } else if (tab[i].equals("(")) { // gdy nawias otwierajacy
                    pos = 0;
                    bracket++; // dodajemy, gdy otwarcie nowego nawiasu
                } else {
                    return false;
                }
            } else if (pos==1) {
                if ("^*/%+-<>?&|=".contains(tab[i])) { // gdy operator jest dwuargumentowy
                    pos = 0;
                } else if (tab[i].equals(")")) { // gdy nawias zamykajacy
                    if (bracket==0) { // gdy nie bylo jeszcze nawiasu otwierajacego
                        return false;
                    }
                    pos = 1;
                    bracket--; // usuwamy, bo zakonczylismy dany nawias
                } else {
                    return false;
                }
            } else if (pos==2) {
                if (priority(tab[i]) == 10) { // gdy operand
                    pos = 1;
                } else if (priority(tab[i]) == 7) { // gdy operator jest jednoargumentowy
                    pos = 2;
                } else if (tab[i].equals("(")) { // gdy nawias otwierajacy
                    bracket++; // dodajemy, gdy otwarcie nowego nawiasu
                    pos = 0;
                } else {
                    return false;
                }
            }
        }

        if (bracket>0) { // gdy byl min. 1 nawias otwierajacy, po ktorym nie nastapil zamykajacy
            return false;
        }

        if (pos==1) { // gdy na pozycji 1 w automacie skonczonym -> wyrazenie jest poprawne
            return true;
        }

        return false;
    }

    public static boolean ONPisCorrect (String tab[]) { // sprawdza poprawnosc wyrazenia ONP
        int n = tab.length; // ilosc elementow (operandow i operatorow) w danym wyrazeniu
        StackArray toStack = new StackArray(n); // tworzymy stos, aby dokonac operacji konwersji
        String out = ""; // bedziemy przechowywac wyrazenie, ktore bedziemy wrzucac na stos

        for (int i=0; i<n; i++) {
            if (priority(tab[i])==10) { // gdy jest operandem, wstawiamy na stos
                toStack.push(tab[i]);
            } else { // gdy jest operatorem
                if (priority(tab[i])==7) { // priorytet 7 odpowiada operatorom jednoargumentowym ! i ~
                    out += tab[i]; // dodajemy operator do wyrazenia, ktore znajdzie sie na stosie

                    if (toStack.isempty()) { // gdy stos jest pusty, nie mozemy pobrac argumentu
                        return false;
                    }

                    out += toStack.pop(); // dodajemy jeden argument do wyrazenia
                    toStack.push(out); // wstawiamy cale wyrazenie na stos

                } else { // dla wyrazenia dwuargumentowego
                    if (toStack.isempty()) { // gdy stos jest pusty, nie mozemy pobrac argumentu
                        return false;
                    }

                    out += toStack.pop(); // dodajemy do wyrazenia pierwszy argument ze stosu
                    out += tab[i]; // dodajemy do wyrazenia operator

                    if (toStack.isempty()) { // gdy stos jest pusty, nie mozemy pobrac argumentu
                        return false;
                    }

                    out += toStack.pop(); // dodajemy do wyrazenia drugi argument ze stosu
                    toStack.push(out); // wstawiamy cale wyrazenie na stos
                }
            }
        }

        toStack.pop();
        if (toStack.isempty()) {
            return true;
        }

        return false;
    }

    public static void INFtoONP(String tab[]) { // zamiana notacji INF na ONP
        int n = tab.length; // ilosc elementow (operandow i operatorow) w danym wyrazeniu
        String output = "ONP: "; // deklarujemy zmienna, ktora bedziemy pozniej wypisywac na ekran
        StackArray toStack = new StackArray(n); // tworzymy stos, aby dokonac operacji konwersji


        for (int i=0; i<n; i++) { // rozpatrujemy kolejne elementy tablicy - czy sa operatorami czy operandami
            if (priority(tab[i])==10) { // jezeli dany element tablicy jest operandem
                // dodajemy znak do wynikowego stringa i spacje, ktora po nim nastepuje
                output += tab[i];
                output += " ";

            } else { // jezeli dany element tablicy jest operatorem
                if (toStack.isempty()) { // jezeli stos jest pusty wkladamy pierwszy napotkany operator
                    toStack.push(tab[i]);
                } else { // jezeli stos nie jest pusty
                    if (tab[i].equals("(")) { // gdy operator to nawias otwierajacy - wkladamy na stos
                        toStack.push(tab[i]);
                        continue; // przechodzi do kolejnej iteracji i, rozwaza kolejny element tablicy
                    }

                    if ("!~^*/%+-><?&|=".contains(tab[i])) { // gdy operator (op1) nie jest ktoryms z nawiasow
                        if (side(tab[i])==1) { // gdy operator op1 jest lewostronny
                            /*
                             * zdejmujemy ze stosu wszystkie operatory o priorytecie niemniejszym niz priorytet
                             * operatora op1 i dopisujemy na wyjscie, jezeli napotkamy nawias poczatkowy
                             * lub priorytet jest mniejszy, przerywamy petle
                             */
                            while (!toStack.isempty()) {
                                if (priority(toStack.top())>=priority(tab[i])) {
                                    String t = toStack.top();
                                    if (t.equals("(")) {
                                        break;
                                    }
                                    output += toStack.pop();
                                    output += " ";
                                } else {
                                    break;
                                }
                            }
                        } else if (side(tab[i])==2) { // gdy operator op1 jest prawostronny
                            /*
                             * zdejmujemy ze stosu wszystkie operatory o priorytecie wiekszym niz priorytet
                             * operatora op1 i dopisujemy na wyjscie, jezeli napotkamy nawias poczatkowy
                             * lub priorytet jest mniejszy, przerywamy petle
                             */
                            while (!toStack.isempty()) {
                                if (priority(toStack.top())>priority(tab[i])) {
                                    String t = toStack.top();
                                    if (t.equals("(")) {
                                        break;
                                    }
                                    output += toStack.pop();
                                    output += " ";
                                } else {
                                    break;
                                }
                            }
                        }
                        toStack.push(tab[i]); // przenosimy operator op1 na stos
                        continue; // przechodzi do kolejnej iteracji i, rozwaza kolejny element tablicy
                    }

                    if (tab[i].equals(")")) { // gdy napotkamy nawias zamykajacy
                        /*
                         * petla zdejmuje ze stosu i dopisuje na wyjscie wszystkie operatory az do napotkania
                         * pierwszego nawiasu otwierajacego, ktory pomija (sciaga go ze stosu bez zapisywania
                         * na wyjscie)
                         */
                        while (!toStack.isempty()) {
                            String t = toStack.top();
                            if (t.equals("(")) {
                                toStack.pop();
                                break;
                            }
                            output += toStack.pop();
                            output += " ";
                        }
                    }
                }
            }
        }

        // petla przepisuje ze stosu wszystko, co na nim zostalo po zakonczeniu wczytywania wyrazenia
        while (!toStack.isempty()) {
            output += toStack.pop();
            if (!toStack.isempty()) { // zeby na koncu nie bylo spacji
                output += " ";
            }
        }

        System.out.println(output); // drukuje na ekran wynik konwersji INF do ONP
    }

    public static void ONPtoINF(String tab[]) { // zamiana notacji ONP na INF
        int n = tab.length; // ilosc elementow (operandow i operatorow) w danym wyrazeniu
        StackArray toStack = new StackArray(n); // tworzymy stos, aby dokonac operacji konwersji
        StackInt priorityStack = new StackInt(n); // tworzymy stos priorytetow
        String tmp = ""; // przechowuje wyrazenie powstale w danej iteracji

        for (int i=0; i<n; i++) {
            if (priority(tab[i])==10) { // jest operandem
                toStack.push(tab[i]); // wkladamy na stos
                priorityStack.push(priority(tab[i])); // dodajemy priorytet operanda na stos

            } else { // jest operatorem
                if (side(tab[i])==2) { // operator prawostronny
                     if (priority(tab[i]) == 7) { // gdy operator prawostronny jednoargumentowy
                         /*
                          * gdy ostatnio dodany do wyrazenia operator ma mniejszy priorytet niz
                          * operator obecnie rozwazany
                          */
                         if (priorityStack.top() < priority(tab[i])) {
                             // dodajemy operator przed poprzednie wyrazenie, ktore dodatkowo ujmujemy w nawias
                             tmp = tab[i] + " ( " + toStack.pop() + " )";
                             priorityStack.pop(); // sciagamy priorytet ostatnio dodanego operatora

                         } else { // gdy priorytet ostatnio dodanego operatora jest niemniejszy
                             tmp = tab[i] + " " + toStack.pop(); // dodajemy operator przed wyrazenie
                             priorityStack.pop(); // sciagamy priorytet ostatnio dodanego operatora
                         }
                     } else { // gdy operator prawostronny dwuargumentowy
                         /*
                          * gdy ostatnio dodany do wyrazenia operator ma mniejszy priorytet niz
                          * operator obecnie rozwazany
                          */
                         if (priorityStack.top() < priority(tab[i])) {
                             // dodajemy operator przed poprzednie wyrazenie, ktore dodatkowo ujmujemy w nawias
                             tmp = tab[i] + " ( " + toStack.pop() + " )";
                             priorityStack.pop(); // sciagamy priorytet

                             // gdy wyrazenie, ktore ma byc z lewej ma mniejszy priorytet
                             if (priorityStack.top() < priority(tab[i])) {
                                 // dodajemy nawiasy do wyrazenia i doklejamy je przed operator
                                 tmp = "( " + toStack.pop() + " ) " + tmp;
                                 priorityStack.pop(); // sciagamy priorytet ostatnio dodanego operatora
                             } else { // wyrazenie ma wiekszy priorytet
                                 tmp = toStack.pop() + " " + tmp; // doklejamy wyrazenie przed operator
                                 priorityStack.pop(); // sciagamy priorytet
                             }

                         } else { // gdy priorytet ostatnio dodanego operatora jest wiekszy
                             tmp = tab[i] + " " + toStack.pop(); // dodajemy operator przed wyrazenie
                             priorityStack.pop(); // sciagamy priorytet ostatnio dodanego operatora

                             // gdy wyrazenie, ktore ma byc z lewej ma mniejszy priorytet
                             if (priorityStack.top() < priority(tab[i])) {
                                 // dodajemy nawiasy do wyrazenia i doklejamy je przed operator
                                 tmp = "( " + toStack.pop() + " ) " + tmp;
                                 priorityStack.pop(); // sciagamy priorytet
                             } else { // wyrazenie ma wiekszy priorytet
                                 tmp = toStack.pop() + " " + tmp; // doklejamy wyrazenie przed operator
                                 priorityStack.pop(); // sciagamy priorytet
                             }
                         }
                     }

                } else { // gdy operator dwuargumentowy
                    /*
                     * gdy ostatnio dodany operator w wyrazeniu (wyrazenie ostatnio dodane na stos), ktore chcemy
                     * wlozyc jako drugi argument ma niewiekszy priorytet niz aktualny operator
                     */
                    if (priorityStack.top() <= priority(tab[i])) {
                        /*
                         * poprzednio dodane wyrazenie dodatkowo ujmujemy w nawias i wstawiamy przed nim operator
                         */
                        tmp = tab[i] + " ( " + toStack.pop() + " )";
                        priorityStack.pop(); // sciagamy priorytet ostatnio dodanego operatora

                        /*
                         * gdy ostatnio dodany operator w wyrazeniu, ktore chcemy wlozyc jako pierwszy argument
                         * ma mniejszy priorytet niz aktualny operator
                         */
                        if (priorityStack.top() < priority(tab[i])) {
                            tmp = "( " + toStack.pop() + " ) " + tmp; // dodajemy wyrazenie przed operatorem i nawias
                            priorityStack.pop(); // sciagamy priorytet ostatnio dodanego operatora
                        } else { // gdy priorytet ostatnio dodanego operatora jest wiekszy
                            tmp = toStack.pop() + " " + tmp; // dodajemy wyrazenie przed operatorem
                            priorityStack.pop(); // sciagamy priorytet ostatnio dodanego operatora
                        }
                    } else { // gdy priorytet ostatnio dodanego operatora jest wiekszy
                        tmp = tab[i] + " " + toStack.pop(); // wkladamy wyrazenie do zmiennej tymczasowej
                        priorityStack.pop();

                        /*
                         * gdy ostatnio dodany operator w wyrazeniu, ktore chcemy wlozyc jako pierwszy argument
                         * ma mniejszy priorytet niz aktualny operator
                         */
                        if (priorityStack.top() < priority(tab[i])) {
                            tmp = "( " + toStack.pop() + " ) " + tmp; // dodajemy wyrazenie przed operatorem i nawias
                            priorityStack.pop(); // sciagamy priorytet ostatnio dodanego operatora
                        } else { // gdy priorytet ostatnio dodanego operatora jest wiekszy
                            tmp = toStack.pop() + " " + tmp; // dodajemy wyrazenie przed operatorem
                            priorityStack.pop(); // sciagamy priorytet ostatnio dodanego operatora
                        }
                    }
                }
                toStack.push(tmp); // wrzucamy aktualne wyrazenie na stos
                priorityStack.push(priority(tab[i])); // wrzucamy priorytet aktualnego operatora na stos
            }
        }


        System.out.println("INF: " + toStack.pop()); // drukuje na ekran wynik konwersji ONP do INF
    }

    public static int side (String sign) { // okresla czy operator jest lewo- czy prawostronny
        if ("!~^=".contains(sign)) {
            return 2; // zwraca 2 gdy operator prawostronny
        } else {
            return 1; // zwraca 1 gdy operator lewostronny
        }

    }

    public static int priority (String sign) { // funkcja okresla priorytet operatorow i operandow
        if (sign.charAt(0) >= 'a' && sign.charAt(0) <= 'z') { // priorytet operandow = 10
            return 10;
        } else if ("^".equals(sign)) {
            return 9;
        } else if ("()".contains(sign)) { // priorytety operatorow, najwyzszy priorytet = najwieksza liczba
            return 8;
        } else if ("!~".contains(sign)) { // priorytet ten odpowiada operatorom jednoargumentowym
            return 7;
        } else if ("*/%".contains(sign)) {
            return 6;
        } else if ("+-".contains(sign)) {
            return 5;
        } else if ("<>".contains(sign)) {
            return 4;
        } else if ("?".equals(sign)) {
            return 3;
        } else if ("&".equals(sign)) {
            return 2;
        } else if ("|".equals(sign)) {
            return 1;
        } else {
            return 0;
        }
    }
}

class StackArray { // stos tablicowy wykorzystywany do przechowywania znakow z danej notacji
    private int maxSize; // rozmiar tablicy zawierajacej stos
    private String[] stackArray; // tablica zawierajaca stos
    private int top; // indeks szczytu stosu

    public StackArray(int size) { // konstruktor - Create()
        maxSize = size; // ustawiamy rozmiar tablicy
        stackArray = new String[maxSize]; // tworzymy tablice dla elementow
        top = -1; // na razie brak elementow (rosnie w gore)
    }

    public void push(String x) { // wstawianie element na szczyt stosu
        stackArray[++top] = x;
    }

    public String pop() { // usuwanie elementu ze szczytu stosu
        return stackArray[top--];
    }

    public boolean isempty() { // zwraca true, jezeli stos pusty, false jesli cos jest na stosie
        if (top<0) {
            return true;
        }
        return false;
    }

    public String top() { // zwraca wartosc na szczycie stosu
        return stackArray[top];
    }
}

class StackInt { // stos tablicowy wykorzystywany do przechowywania priorytetow
    private int maxSize; // rozmiar tablicy zawierajacej stos
    private int[] stackArray; // tablica zawierajaca stos
    private int top; // indeks szczytu stosu

    public StackInt(int size) { // konstruktor - Create()
        maxSize = size; // ustawiamy rozmiar tablicy
        stackArray = new int[maxSize]; // tworzymy tablice dla elementow
        top = -1; // na razie brak elementow (rosnie w gore)
    }

    public void push(int x) { // wstawianie element na szczyt stosu
        stackArray[++top] = x;
    }

    public int pop() { // usuwanie elementu ze szczytu stosu
        return stackArray[top--];
    }

    public boolean isempty() { // zwraca true, jezeli stos pusty, false jesli cos jest na stosie
        if (top<0) {
            return true;
        }
        return false;
    }

    public int top() { // zwraca wartosc na szczycie stosu
        return stackArray[top];
    }
}

/*
--- input ---
20
INF: a+b+!c
INF: x  = a=c=~~d
INF: x <> a * b
INF: a^b^c = ,.!r + y
INF: (x+y) '; * (x-y)
INF: (x+   r )( a*b+c
INF: x^y^(a+b+c)
INF: !(x+y+z)/~(a%b)
INF: a&(b-c)^(x+y%z)
INF: a|b-a&b+ ,.!c|d
ONP: (c)def++*
ONP: ab-de*-+
ONP: a bd+,,,,(!-
ONP: xyze+==
ONP: rst<>uw-+
ONP: (c) d+ef+*
ONP: defghi|-+&*
ONP: abc-+d%ef--
ONP: x!y!-a~~-b*
ONP: h x,,,,,,,,e-+!r/t%

--- output ---
ONP: a b + c ! +
ONP: x a c d ~ ~ = = =
ONP: error
ONP: a b c ^ ^ r ! y + =
ONP: x y + x y - *
ONP: error
ONP: x y a b + c + ^ ^
ONP: x y + z + ! a b % ~ /
ONP: a b c - x y z % + ^ &
ONP: a b a - b c ! + & | d |
INF: c * ( d + ( e + f ) )
INF: error
INF: a - ! ( b + d )
INF: x = y = z + e
INF: ( r > ( s < t ) ) + ( u - w )
INF: ( c + d ) * ( e + f )
INF: d * ( e & f + ( g - ( h | i ) ) )
INF: ( a + ( b - c ) ) % d - ( e - f )
INF: ( ! x - ! y - ~ ~ a ) * b
INF: ! ( h + ( x - e ) ) / r % t
*/