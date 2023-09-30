import socket

# Create a socket object
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Get local machine name
host = socket.gethostname()

# Connect to the server
client_socket.connect(("3.83.224.60", 8000))

# Send a question to the server
question = "What is the answer to life, the universe, and everything?"
s="chills, vomiting, fatigue, weight_loss, cough, high_fever, breathlessness, sweating, loss_of_appetite, mild_fever, yellowing_of_eyes, swelled_lymph_nodes, malaise, phlegm, chest_pain, blood_in_sputum"

client_socket.send(s.encode())

# Receive the answer from the server
answer = client_socket.recv(1024)

# Print the answer
print(answer.decode())

# Close the connection with the server
client_socket.close()
