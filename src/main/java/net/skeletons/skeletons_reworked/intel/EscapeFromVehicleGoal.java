package net.skeletons.skeletons_reworked.intel;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;

/**
 * @Author = ASEStefan
 */
public class EscapeFromVehicleGoal extends Goal
{
    private final Mob mob;

    public EscapeFromVehicleGoal(Mob mob)
    {
        this.mob = mob;
    }

    @Override
    public boolean canUse()
    {
        return this.mob.getVehicle() != null && this.mob.getVehicle() instanceof Boat || this.mob.getVehicle() instanceof Minecart;
    }

    @Override
    public void start()
    {
        this.mob.stopRiding();
    }
}
