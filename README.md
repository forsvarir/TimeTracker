# TimeTracker
Time Tracking App

# Some use cases

#### I want to be able to use some known TrackableActivities
#### I want to be able to indicate that I've started a new activity
#### I want to be able to see what activities I've started

#### I want to be able to add new TrackableActivity types
#### I want to be able to remove TrackableActivity types so that they can't be used anymore


## Dictionary

- TrackableActivity - 'Activity' is an overloaded term. The tracker needs to track things that
  are happening. Tasks might have worked, however doesn't seem to apply to items such as reading
  or eating. Activity seems a bit closer. The application will know about activities that can be
  tracked.
  
## Database

Assume all tables have a primary key, named `id`. References to other tables will be named 
`tableName_id`.

- TrackableActivities
  - Name

- ActivityEvents
  - TrackableActivities_id
  - EventTime
  - EventType  (start/stop, may be able to just use start initially)
    
