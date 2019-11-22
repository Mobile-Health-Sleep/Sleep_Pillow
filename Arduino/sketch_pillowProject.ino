void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  while(Serial.available() == 0) {}
  int num = Serial.parseInt();

  factorial(num);
  Serial.println();
  fibNum(num);
}

void loop() {
  // put your main code here, to run repeatedly:

}

void factorial(int x) {
  output(0,1);
  
  long long val = 1;
  for(long long i = 1; i <= x; i++) {
    val *= i;
    output(i, val);
  }
}

void fibNum(int x) {
  long long a = 0, b = 1, c;
  if(x == 0)
    return a;
   for(long i=2; i <= x; i++) {
    c = a+b;
    a = b;
    b=c;
   }

   outputFib(x, c);
}

void output(long long a, long long p){
  Serial.print("The factorial of ");
  Serial.print(a);
  Serial.print(" = ");
  Serial.println(p);
}

void outputFib(long a, long p){
  Serial.print("The Fibonacci Number ");
  Serial.print(a);
  Serial.print(" = ");
  Serial.println(p);
}
