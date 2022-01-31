import numpy as np

print("\n")

#Could/Should be a lamba function
def substitute_cost(source_letter, target_letter):
    if source_letter == target_letter:
        return 0
    return 1

#Given a source word and target word, return the minimum edit distance
def get_minimum_edit_distance(source_word, target_word):
    minimum_distance = float('inf')
    
    n = len(source_word) 
    m = len(target_word) 

    D = np.zeros([n+1, m+1])
    print(f"Initalized Distance Matrix\n{D}\n")
    
    for i in range(1, n+1):
        D[i][0] = D[i-1][0] + 1
    print(f"zeroth column initalized\n{D}\n")
          
    for j in range(1, m+1):
        D[0][j] = D[0][j-1] + 1
    print(f"zeroth row initalized\n{D}\n")
        
    for i in range(1,n+1):
        for j in range (1,m+1):
            D[i][j] = min([D[i-1][j] + 1, 
                          D[i-1][j-1] + substitute_cost(source_word[i-1], target_word[j-1]), 
                          D[i][j-1] + 1])

    print(f"Completed\n{D}\n")
    return D[n][m]

source_word = 'leda'
target_word = 'deal'
MED = get_minimum_edit_distance(source_word, target_word)
print(f"MED from {source_word} to {target_word} is {MED}")

source_word = 'drive'
target_word = 'brief'
MED = get_minimum_edit_distance(source_word, target_word)
print(f"MED from {source_word} to {target_word} is {MED}")