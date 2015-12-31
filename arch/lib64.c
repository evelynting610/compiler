#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void *b_intToString_0(long i) {
  char *sval;
  int result = asprintf (&sval, "%ld", i);
  if (result == -1) {
    fprintf (stderr, "Error in converting int to string.\n");
    exit (1);
  }
  return sval;
}

void *b_stringConcatenate_0(char *c, char *d) {
  char *sval;
  int result = asprintf (&sval, "%s%s", c, d);
  if (result == -1) {
    fprintf (stderr, "Error in concatenating strings\n");
    exit (1);
  }
  return sval;
}

void *b_createArray_0(long size) {
  long *p = malloc ((size+1)*8);
  if (p == NULL) {
    fprintf (stderr, "Error in allocating array");
    exit (1);
  }
  *p = size;
  return p+1;
}

long b_string_length_0(char *s) {
  return strlen(s);
}

void b_printString_0 (char *p) {
  printf ("%s", p);
}

