Solution:

1 add a parameter about the index to receive the top index card (For example, if i input 2, 咁我會攞到第二top嘅卡)
2 change the part which calls this function

Step to reproduce:

1. human/cpu plays a wild draw 4 card and choose any color
2. if next player choose to challenge , it will always succeed since the card being checked with players' hand is the topcard which is the wilddraw4.
