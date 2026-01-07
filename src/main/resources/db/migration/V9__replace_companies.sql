DELETE FROM companies;

-- Replace companies with curated high-volume losers
DELETE FROM companies;

INSERT INTO companies (symbol, logo, description, tone_tag) VALUES

('AMC',  NULL, NULL, 'meme_crash'),
('GME',  NULL, NULL, 'meme_crash'),
('BB',   NULL, NULL, 'meme_crash'),
('NOK',  NULL, NULL, 'meme_crash'),
('PLUG', NULL, NULL, 'meme_crash'),

('RIVN', NULL, NULL, 'hype_ev_burn'),
('LCID', NULL, NULL, 'hype_ev_burn'),
('NKLA', NULL, NULL, 'hype_ev_burn'),
('CHPT', NULL, NULL, 'hype_ev_burn'),
('QS',   NULL, NULL, 'hype_ev_burn'),

('PARA', NULL, NULL, 'slow_bleed'),
('WBD',  NULL, NULL, 'slow_bleed'),
('VZ',   NULL, NULL, 'slow_bleed'),
('T',    NULL, NULL, 'slow_bleed'),
('DIS',  NULL, NULL, 'slow_bleed'),


('OTLK', NULL, NULL, 'biotech_grinder'),
('KZIA', NULL, NULL, 'biotech_grinder'),
('BCAB', NULL, NULL, 'biotech_grinder'),
('VYNE', NULL, NULL, 'biotech_grinder'),
('SAVA', NULL, NULL, 'biotech_grinder');
