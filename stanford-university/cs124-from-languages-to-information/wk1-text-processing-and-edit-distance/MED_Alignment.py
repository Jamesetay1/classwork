import numpy as np

print("\n")

#Could/Should be a lamba function
def substitute_cost(source_letter, target_letter):
    if source_letter == target_letter:
        return 0
    return 2

#Given a source word and target word, return the minimum edit distance
def get_minimum_edit_distance(source_word, target_word):
    minimum_distance = float('inf')
    
    n = len(source_word) 
    m = len(target_word) 

    D = np.zeros([n+1, m+1])
    #In backtrace, 1 = delete, 2 = insert, 4 = substitute
    B = np.zeros([n+1, m+1])
    
    for i in range(1, n+1):
        D[i][0] = D[i-1][0] + 1
        B[i][0] = 1
          
    for j in range(1, m+1):
        D[0][j] = D[0][j-1] + 1
        B[0][j] = 2
        
    for i in range(1,n+1):
        for j in range (1,m+1):  
            delete = D[i-1][j] + 1
            add = D[i][j-1] + 1
            substitute = D[i-1][j-1] + substitute_cost(source_word[i-1], target_word[j-1])
            
            minimum = min(delete,add,substitute)
            D[i][j] = minimum
            
            backtrace_val = 0
            if delete == minimum: backtrace_val = backtrace_val + 1
            if add == minimum: backtrace_val = backtrace_val + 2
            if substitute == minimum: backtrace_val = backtrace_val + 4
            B[i][j]= backtrace_val      
                    
    
    #If I ever want to use: ↖  
    print(f"Completed Distance\n{D}\n")
    print(f"Completed Backtrace: 1:Delete(Up), 2:Insert(Left), 4:Substitute(Diag)\n{B}\n")
    
    source_alignment = []
    target_alignment = []
    action = []
    
    i = n-1
    j = m-1
    while i>0 or j>0:
        backtrace_val = B[i][j]
        #Has a substituion (diagonal)
        if backtrace_val > 3: 
            source_alignment.insert(0, source_word[i]) 
            target_alignment.insert(0, target_word[j])
            
            if(source_word[i] == source_word[j]): action.insert(0, '_')
            else: action.insert(0, 's')
           
            i -= 1
            j -= 1
            
        #Has an insertion (left)
        elif backtrace_val > 1:
            source_alignment.insert(0, '*') 
            target_alignment.insert(0, target_word[j])
            
            action.insert(0, 'i')
            
            j -= 1
            
        #Has a deletion (up)
        else:
            source_alignment.insert(0, source_word[i]) 
            target_alignment.insert(0, '*')
            
            action.insert(0, 'd')
            
            i -= 1
    
    #Spaghetti - need to finish the actual edge case for start of word      
    if i == 0 and j == 0:
        source_alignment.insert(0, source_word[0]) 
        target_alignment.insert(0, target_word[0])
        action.insert(0, '_')
       
            
    return D[n][m], source_alignment, target_alignment, action

source_word = 'leda'
target_word = 'deal'
MED, source, target, action = get_minimum_edit_distance(source_word, target_word)
print(f"MED from {source_word} to {target_word} is {MED}")
print(source)
print(target)
print(action)

source_word = 'intention'
target_word = 'execution'
MED, source, target, action = get_minimum_edit_distance(source_word, target_word)
print(f"MED from {source_word} to {target_word} is {MED}")
print(source)
print(target)
print(action)