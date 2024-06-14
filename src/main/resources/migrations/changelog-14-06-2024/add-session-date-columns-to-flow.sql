ALTER TABLE flow
ADD COLUMN IF NOT EXISTS lessons_start_date date NOT NULL DEFAULT 'epoch',
ADD COLUMN IF NOT EXISTS session_start_date date NOT NULL DEFAULT 'infinity',
ADD COLUMN IF NOT EXISTS session_end_date date NOT NULL DEFAULT 'infinity';