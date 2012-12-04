  #!/bin/bash
  green="\x1b[42m";
  default="\x1b[0m";
  color=$green;
  put_T()
  {
   echo -e "\x1b[2J\x1b[""$1""H";  
   mov="\x1b["$2"C";
   T=$mov$color"\x20\x20\x20\x20\x20\x20"$default"\x0a$mov\x20\x20"$color"\x20\x20"$default"\x20\x20";
   echo -e "$T";
  }
  for((i=1;i<20;i++));do
    {
   H=$i;
   off=$(expr $i + 2);
   put_T $i $off;
   echo -e "\x1b[21H";  
    }
    sleep 0.4;
  done
