UPDATE user
SET Tier = CASE
               WHEN Tier = '0' THEN 'BRONZE'
               WHEN Tier = '1' THEN 'SLIVER'
               WHEN Tier = '2' THEN 'GOLD'
               WHEN Tier = '3' THEN 'PLATINUM'
               WHEN Tier = '4' THEN 'DIAMOND'
               ELSE Tier
    END;
