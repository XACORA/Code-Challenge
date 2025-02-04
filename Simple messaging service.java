def generate_key():
    # Generate a key for encryption (using a simple base64 encoding for illustration)
    return 'simple-encryption-key'

def encrypt_message(key, message):
    # Encrypt the message using the key (this is a placeholder for real encryption)
    encrypted_message = message.encode('utf-8').decode('utf-8')
    return encrypted_message

def decrypt_message(key, encrypted_message):
    # Decrypt the message using the key (this is a placeholder for real decryption)
    decrypted_message = encrypted_message.encode('utf-8').decode('utf-8')
    return decrypted_message

# Function to filter messages based on a keyword
def filter_messages(messages, keyword):
    return [msg for msg in messages if keyword.lower() in msg.lower()]

# Create a mock client to simulate the Hedera client behavior
class Client:
    def _init_(self):
        self.operator = None

    @staticmethod
    def for_testnet():
        return Client()

    def set_operator(self, account_id, private_key):
        self.operator = (account_id, private_key)

    def execute(self, transaction):
        # Simulate executing a transaction (stub for Hedera interaction)
        return transaction

class TopicCreateTransaction:
    def execute(self, client):
        # Simulate topic creation (return a mock topic ID)
        return MockTransactionResponse("0.0.34567")

class TopicMessageSubmitTransaction:
    def set_topic_id(self, topic_id):
        self.topic_id = topic_id
        return self

    def set_message(self, message):
        self.message = message
        return self

    def execute(self, client):
        # Simulate sending a message (stub for Hedera interaction)
        print(f"Sent message to topic {self.topic_id}: {self.message}")
        return MockTransactionResponse("Message Sent")

class TopicMessageQuery:
    def set_topic_id(self, topic_id):
        self.topic_id = topic_id
        return self

    def set_start_time(self, start_time):
        self.start_time = start_time
        return self

    def execute(self, client):
        # Simulate retrieving messages from a topic (stub for Hedera interaction)
        return [
            MockMessage("Hello, Hedera!"),
            MockMessage("Learning HCS"),
            MockMessage("Message 3")
        ]

class MockTransactionResponse:
    def _init_(self, result):
        self.result = result
        self.topic_id = "0.0.34567"

    def get_receipt(self, client):
        return self

class MockMessage:
    def _init_(self, message):
        self.message = message

# Main code to run the service
def main():
    # Create Hedera client
    client = Client.for_testnet()
    
    # Set operator with mock account details
    client.set_operator("YOUR_ACCOUNT_ID", "YOUR_PRIVATE_KEY")
    
    # Create a new topic (only done once)
    create_topic_transaction = TopicCreateTransaction()
    create_topic_response = create_topic_transaction.execute(client)
    topic_id = create_topic_response.get_receipt(client).topic_id
    print(f"Topic Created: {topic_id}")
    
    # Generate encryption key (you can save this key to reuse later)
    encryption_key = generate_key()
    
    # Send some messages
    send_message(client, topic_id, "Hello, Hedera!", encryption_key)
    send_message(client, topic_id, "Learning HCS", encryption_key)
    send_message(client, topic_id, "Message 3", encryption_key)
    
    # Retrieve messages
    messages = retrieve_messages(client, topic_id, encryption_key)
    
    # Filter messages for the keyword 'Hedera'
    filtered_messages = filter_messages(messages, "Hedera")
    
    print("\nMessages Received:")
    for msg in filtered_messages:
        print(msg)

# Send message function
def send_message(client, topic_id, message, encryption_key):
    encrypted_message = encrypt_message(encryption_key, message)
    
    # Create and send message to the topic
    transaction = TopicMessageSubmitTransaction().set_topic_id(topic_id).set_message(encrypted_message)
    transaction.execute(client)

# Retrieve messages function
def retrieve_messages(client, topic_id, encryption_key):
    messages = []
    
    # Query messages from the topic
    query = TopicMessageQuery().set_topic_id(topic_id).set_start_time(0)
    response = query.execute(client)
    
    for msg in response:
        decrypted_message = decrypt_message(encryption_key, msg.message)
        messages.append(decrypted_message)
    
    return messages

if _name_ == "_main_":
    main()