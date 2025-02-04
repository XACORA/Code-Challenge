from datetime import datetime

class Event:
    def _init_(self, name, start_time, end_time):
        self.name = name
        self.start_time = start_time
        self.end_time = end_time

    def _repr_(self):
        return f"{self.name} ({self.start_time.strftime('%Y-%m-%d %H:%M')} to {self.end_time.strftime('%Y-%m-%d %H:%M')})"

class EventScheduler:
    def _init_(self):
        self.events = []

    def add_event(self, event):
        if self._check_conflict(event):
            print(f"Conflict detected! Could not add event: {event}")
        else:
            self.events.append(event)
            print(f"Event added: {event}")

    def _check_conflict(self, new_event):
        for existing_event in self.events:
            if (new_event.start_time < existing_event.end_time and 
                new_event.end_time > existing_event.start_time):
                return True
        return False

    def list_events(self):
        if not self.events:
            print("No events scheduled.")
        else:
            print("Scheduled Events:")
            for event in sorted(self.events, key=lambda e: e.start_time):
                print(event)

def main():
    scheduler = EventScheduler()

    while True:
        print("\n1. Add Event")
        print("2. List Events")
        print("3. Exit")
        choice = input("Choose an option: ")

        if choice == "1":
            name = input("Enter event name: ")
            start_time = input("Enter start time (YYYY-MM-DD HH:MM): ")
            end_time = input("Enter end time (YYYY-MM-DD HH:MM): ")

            try:
                start_time = datetime.strptime(start_time, "%Y-%m-%d %H:%M")
                end_time = datetime.strptime(end_time, "%Y-%m-%d %H:%M")
                event = Event(name, start_time, end_time)
                scheduler.add_event(event)
            except ValueError:
                print("Invalid date format. Please use YYYY-MM-DD HH:MM.")
        elif choice == "2":
            scheduler.list_events()
        elif choice == "3":
            print("Exiting...")
            break
        else:
            print("Invalid choice. Please try again.")

if _name_ == "_main_":
    main()