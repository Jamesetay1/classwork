import re

print("ELIZA v1 by James,  to say goodbye")

input_val = input("Hello, I am ELIZA. What problem are you having today?\n")

while input_val != 'QUIT':
    if re.compile(r'I\'m (sad|depressed)').search(input_val):
        question = re.sub(r'(.*) I\'m (sad|depressed) (.*)', 'I am sorry to hear that you are \g<2>.', input_val)
    elif re.compile(r'I am (sad|depressed)').search(input_val):
        question = re.sub(r'(.*) I am (sad|depressed) (.*)', 'Why do you think that you are \g<2>?', input_val)
    elif re.compile(f'always').search(input_val):
        question = 'Can you think of a specific example?'
    elif re.compile(f'all').search(input_val):
        question = 'In what way?'
    elif re.compile(f'my').search(input_val):
        question = re.sub(r'(.*) my (.*)', 'Your \g<2>?', input_val)
        question = re.sub(r'(.*) me (.*)', '\g<1> you \g<2>', question)
    elif re.compile(f'bye|leave|goodbye').search(input_val):
        print('Eliza says: Bye then...')
        break
    else: 
        question = f'really?'
    input_val = input("Eliza says: " + question+"\n")
    
    

print("Eliza says: See you next week!")