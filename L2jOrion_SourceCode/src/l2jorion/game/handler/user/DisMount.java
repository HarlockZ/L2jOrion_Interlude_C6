/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l2jorion.game.handler.user;

import l2jorion.game.datatables.SkillTable;
import l2jorion.game.handler.IUserCommandHandler;
import l2jorion.game.model.actor.instance.L2PcInstance;
import l2jorion.game.network.serverpackets.Ride;
import l2jorion.game.util.Broadcast;

/**
 * Support for /dismount command.
 * 
 * @author Micht
 */
public class DisMount implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		62
	};

	/* (non-Javadoc)
	 * @see l2jorion.game.handler.IUserCommandHandler#useUserCommand(int, l2jorion.game.model.L2PcInstance)
	 */
	@Override
	public synchronized boolean useUserCommand(int id, L2PcInstance activeChar)
	{
		if(id != COMMAND_IDS[0])
			return false;

		if(activeChar.isRentedPet())
		{
			activeChar.stopRentPet();
		}
		else if(activeChar.isMounted())
		{
			if(activeChar.setMountType(0))
			{
				if(activeChar.isFlying())
				{
					activeChar.removeSkill(SkillTable.getInstance().getInfo(4289, 1));
				}

				Ride dismount = new Ride(activeChar.getObjectId(), Ride.ACTION_DISMOUNT, 0);
				Broadcast.toSelfAndKnownPlayersInRadius(activeChar, dismount, 810000/*900*/);
				activeChar.setMountObjectID(0);

				dismount = null;
			}
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see l2jorion.game.handler.IUserCommandHandler#getUserCommandList()
	 */
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}